package com.sojoo.StoreSpotter.jwt.jwt;


import com.sojoo.StoreSpotter.service.redis.RedisService;
import com.sojoo.StoreSpotter.service.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;
import java.util.Collection;

import static com.sojoo.StoreSpotter.util.CookieUtil.addCookie;

//public class JwtFilter extends GenericFilterBean {
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;
    private final static int COOKIE_EXPIRE_SECONDS = 3600;      // 쿠키 존재 시간 1시간 설정
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtFilter doFilter 실행");

        String jwt = null;
        if (request.getCookies() != null) {
            for (Cookie cookie: request.getCookies()) {
                if (cookie.getName().equals("access_token")) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        System.out.println("jwt 토큰 = " + jwt);
        String requestURI = request.getRequestURI();
        System.out.println("doFilter의 requestURI : "+ requestURI);

        String newAccessToken = tokenProvider.validateToken(jwt, response);
        System.out.println("newAccessToken 확인 : " + newAccessToken);

//        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, response)) {
        if (newAccessToken != null) {
            System.out.println("jwt not null 확인");
//            Authentication authentication = tokenProvider.getAuthentication(jwt);
            Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("컨텍스트홀더 테스트: " + SecurityContextHolder.getContext().getAuthentication());
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            addCookie(response, "access_token", newAccessToken, COOKIE_EXPIRE_SECONDS);
            filterChain.doFilter(request, response);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            filterChain.doFilter(request, response);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        log.info("JwtFilter resolveToken 실행");
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}