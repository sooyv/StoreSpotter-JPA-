//package com.sojoo.StoreSpotter.jwt.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//
//// 해당 클래스는 JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달합니다.
//@Slf4j
//@RequiredArgsConstructor
//    private final JwtTokenProvider tokenProvider;
//    private final static String HEADER_AUTHORIZATION = "Authorization";
//    private final static String TOKEN_PREFIX = "Bearer ";
//
//    public void printAllHeaders(HttpServletRequest request) {
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            Enumeration<String> headers = request.getHeaders(headerName);
//            while (headers.hasMoreElements()) {
//                String headerValue = headers.nextElement();
//                System.out.println(headerName + ": " + headerValue);
//            }
//        }
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // 요청 헤더의 Authorization 키의 값 조회
//        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
//        System.out.println("doFilterInternal authorizationHeader 확인: " + authorizationHeader);
////        printAllHeaders(request);
//        // 가져온 값에서 접두사 제거
//        String token = getAccessToken(authorizationHeader);
//        System.out.println("doFilterInternal token 확인: " + token);
//
//        if (token != null && tokenProvider.validToken(token)) {
//            // 토큰이 null이 아니고, validToken()으로 가져온 토큰이 유효한지 확인. 유효하면 토큰으로부터 유저정보를 가져온다.
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            System.out.println("doFilterInternal authentication 확인 : " + authentication);
//            // 인증정보를 관리하는 SecurityContext 에 Authentication 객체를 저장. (인증 정보를 설정)
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            // 인증 정보가 설정된 이후에 컨텍스트 홀더에서 getAuthentication() 메서드를 사용해 인증 정보를 가져오면 유저 객체가 반환
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//
//
//    private String getAccessToken(String authorizationHeader) {
//        System.out.println("getAccessToken 메서드 실행");
//        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
//            System.out.println("getAccessToken : "+ authorizationHeader);
//            return authorizationHeader.substring(TOKEN_PREFIX.length());
//        }
//        // 만약 값이 null이거나 Bearer로 시작하지 않으면 null을 return
//        return null;
//    }
//
//    // Request Header 에서 토큰 정보를 꺼내오기
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
//            return bearerToken.substring(TOKEN_PREFIX.length());
//        }
//        return null;
//    }
//
//}
//
