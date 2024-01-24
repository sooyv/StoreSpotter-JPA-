package com.sojoo.StoreSpotter.config.jwt;

import com.sojoo.StoreSpotter.entity.Member.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        System.out.println("JwtTokenProvider generateToken 실행");
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }


    // JWT 토큰 생성 메서드 - 만료시간, 유저정보
    private String makeToken(Date expiry, Member member) {
        Date now = new Date();
        System.out.println("JwtTokenProvider makeToken 실행");

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ : JWT
                // 내용 iss : propertise 파일에서 설정한 값
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)                   // 내용 iat(발급일시) : 현재시간
                .setExpiration(expiry)              // 내용 exp(만료일자) : expiry 멤버 변수값
                .setSubject(member.getMemberEmail())            // 내용 sub : 유저 email
                .claim("id", member.getMemberId())        // 클레임 id : 유저 id
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }


    // jwt 유효성 검사 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())    // 비밀값으로 복호화
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {         // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }


    // 토큰 기반으로 인증 정보를 가져오는 메서드 - 인증정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);   // 토큰 복호화
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }


    // 토큰 기반으로 유저 id를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }


    private Claims getClaims(String token) {
        return Jwts.parser()    // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }


}
