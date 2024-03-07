package com.sojoo.StoreSpotter.controller.user;


import com.sojoo.StoreSpotter.dto.user.LoginDto;
import com.sojoo.StoreSpotter.jwt.dto.TokenDto;
import com.sojoo.StoreSpotter.jwt.jwt.JwtFilter;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;

import static com.sojoo.StoreSpotter.util.CookieUtil.*;

@Slf4j
@RestController
//@RequestMapping("/")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CookieUtil cookieUtil;
    private final static int COOKIE_EXPIRE_SECONDS = 3600;      // 쿠키 존재 시간 1시간 설정

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, CookieUtil cookieUtil) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.cookieUtil = cookieUtil;
    }

    // 로그아웃
    @Transactional
    @PostMapping("/member/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("logout 실행 - /member/logout");

        try {
            cookieUtil.deleteCookie(request, response, "access_token");
            return ResponseEntity.ok("logout");
        } catch (Exception e) {
            log.error("로그아웃 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 중 오류 발생");
        }
    }

    @PostMapping("/member/login")
    public ResponseEntity<TokenDto> loginProcess(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) throws Exception {

        log.info("loginProcess 동작");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        log.info("loginProcess authenticationToken : " + authenticationToken);

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("loginProcess authentication : " + authentication);

            String accessToken = tokenProvider.createAccessToken(authentication);
            log.info("accessToken create : " + accessToken);
            String refreshToken = tokenProvider.createRefreshToken(authentication);

            Cookie accessTokenCookie = addCookie(response, "access_token", accessToken, COOKIE_EXPIRE_SECONDS);
            log.info("accessTokenCookie create : " + accessTokenCookie);
//            Cookie refreshTokenCookie = cookieUtil.addCookie("refresh_token", refreshToken);

            TokenDto tokenDto = TokenDto.builder()
                    .accessToken(accessTokenCookie)
//                    .refreshToken(refreshTokenCookie)
                    .build();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


//        @GetMapping("/api/token")
//    public String getData(@RequestHeader("Authorization") String authorizationHeader) {
//        // Authorization 헤더에서 JWT 토큰 추출
//        String jwtToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 추출
//        System.out.println("getData : " +jwtToken);
//
//        // JWT 토큰을 검증하고 유효성을 확인하는 로직 수행
//        // 여기서는 단순히 토큰을 반환하는 예시
//        return "Received JWT token: " + jwtToken;
//    }
}