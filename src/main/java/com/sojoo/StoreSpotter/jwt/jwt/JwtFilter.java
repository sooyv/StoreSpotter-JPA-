package com.sojoo.StoreSpotter.jwt.jwt;


import com.sojoo.StoreSpotter.util.CookieUtil;
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
        if (jwt != null && tokenProvider.getExpiredFromToken(jwt).before(now)){
            jwt = tokenProvider.reissueAccessToken(jwt, response);
        }

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("컨텍스트홀더 테스트: " +SecurityContextHolder.getContext().getAuthentication());
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            System.out.println("이것은 false");
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(request, response);
    }


}