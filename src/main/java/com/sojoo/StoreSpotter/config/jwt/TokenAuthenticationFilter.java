package com.sojoo.StoreSpotter.config.jwt;

import com.sojoo.StoreSpotter.controller.jwt.TokenApiController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달합니다.
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                                                        throws ServletException, IOException {
        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);

        // validToken()으로 가져온 토큰이 유효한지 확인
        // 유효하다면 인증정보를 관리하는 시큐리티 컨텍스트에 인증 정보를 설정
        // 인증 정보가 설정된 이후에 컨텍스트 홀더에서 getAuthentication() 메서드를 사용해 인증 정보를 가져오면 유저 객체가 반환
        if (tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        // 만약 값이 null이거나 Bearer로 시작하지 않으면 null을 return
        return null;
    }
}

