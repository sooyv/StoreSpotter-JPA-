package com.sojoo.StoreSpotter.jwt.jwt;


import com.sojoo.StoreSpotter.jwt.exception.JwtErrorCode;
import com.sojoo.StoreSpotter.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtFilter doFilter 실행");

        String jwt = null;
        Cookie cookie = CookieUtil.getCookie(request, "access_token");
        if (cookie != null){
            jwt = cookie.getValue();
        }

        String requestURI = request.getRequestURI();
        System.out.println("doFilter의 requestURI : "+ requestURI);

        // accessToken 만료 여부 확인 후 newAccessToken 발급
        Date now = new Date();
        String newAccessToken = jwt;
        String refreshToken = null;

        if (jwt != null){
            refreshToken = tokenProvider.getRefreshTokenFromAccessToken(newAccessToken);
            if (tokenProvider.getExpiredFromToken(jwt).before(now)){
                newAccessToken = tokenProvider.reissueAccessToken(jwt, response);
            }
        }

        // refreshToken 만료 시 redis 에서 삭제
        if (refreshToken != null && tokenProvider.getExpiredFromToken(refreshToken).before(now)){
            tokenProvider.delRedisFromAccessToken(newAccessToken);
        }

        if(newAccessToken != null) {
            // RefreshToken & AccessToken 유효성 검사 모두 통과
            if (tokenProvider.validRefreshTokenFromAccessToken(newAccessToken) && tokenProvider.validRefreshTokenFromAccessToken(newAccessToken)){
                System.out.println("진입함1");

                Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("컨텍스트홀더 테스트: " + SecurityContextHolder.getContext().getAuthentication());
                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {        // newAccessToken == jwt 일 경우. 즉, 유효하지 않은 refresh 토큰으로 인해 새로운 accessToken을 발급받지 못한 경우
                System.out.println("hereRR");
                try {
                    // refreshToken은 만료되었지만 accessToken이 만료되지 않았을 때 exception
                    tokenProvider.getClaims(newAccessToken);
                    if (refreshToken != null){
                        tokenProvider.getClaims(refreshToken);
                    } else{
                        refreshToken.toUpperCase();
                    }
                } catch (ExpiredJwtException e) {
                    System.out.println("진입함2");
                    e.printStackTrace();
                    request.setAttribute("exception", JwtErrorCode.EXPIRED_TOKEN.getCode());
                } catch (JwtException e) {
                    System.out.println("진입함3");
                    e.printStackTrace();
                    request.setAttribute("exception", JwtErrorCode.INVALID_TOKEN.getCode());
                } catch (NullPointerException e){
                    e.printStackTrace();
                    request.setAttribute("exception", JwtErrorCode.ACCESS_DENIED.getCode());
                }
            }
        } else{
            try{
                newAccessToken.toUpperCase();
            } catch (NullPointerException e){
                e.printStackTrace();
                request.setAttribute("exception", JwtErrorCode.ACCESS_DENIED.getCode());
            }
            System.out.println("여기까지");
        }


        filterChain.doFilter(request, response);
    }


}