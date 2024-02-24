package com.sojoo.StoreSpotter.controller.user;


import com.sojoo.StoreSpotter.dto.user.LoginDto;
import com.sojoo.StoreSpotter.jwt.dto.TokenDto;
import com.sojoo.StoreSpotter.jwt.jwt.JwtFilter;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@RestController
//@RequestMapping("/")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/member/login")
    public ResponseEntity<TokenDto> loginProcess(@Valid @RequestBody LoginDto loginDto) throws Exception {

        System.out.println("loginProcess 동작");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        log.info("loginProcess authenticationToken : " + authenticationToken);

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("loginProcess authentication : " + authentication);

//        String jwt = tokenProvider.createToken(authentication);
            TokenDto tokenDto = new TokenDto(
                    tokenProvider.createAccessToken(authentication),
                    tokenProvider.createRefreshToken(authentication)
            );

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //    @GetMapping("/api/token")
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