package com.sojoo.StoreSpotter.jwt.jwt;


import com.sojoo.StoreSpotter.jwt.dto.ErrorCode;
import com.sojoo.StoreSpotter.util.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.Objects;

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
        if (jwt != null && tokenProvider.getExpiredFromToken(jwt).before(now)){
            newAccessToken = tokenProvider.reissueAccessToken(jwt, response);
        }

        if(jwt != null) {
            if (tokenProvider.mvcIntercepterValid(newAccessToken) && tokenProvider.validateToken(newAccessToken)){
                System.out.println("진입함1");

                Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("컨텍스트홀더 테스트: " + SecurityContextHolder.getContext().getAuthentication());
                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                System.out.println("hereRR");
                try {
                    tokenProvider.getClaims(newAccessToken);
                } catch (ExpiredJwtException e) {
                    System.out.println("진입함2");
                    e.printStackTrace();
                    request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
                } catch (JwtException e) {
                    System.out.println("진입함3");
                    e.printStackTrace();
                    request.setAttribute("exception", ErrorCode.INVALID_TOKEN.getCode());
                }
            }
        } else{
            System.out.println("여기까지");
        }


        filterChain.doFilter(request, response);
    }


}