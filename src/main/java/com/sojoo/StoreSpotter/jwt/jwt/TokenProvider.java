package com.sojoo.StoreSpotter.jwt.jwt;

import com.sojoo.StoreSpotter.service.redis.RedisService;
import com.sojoo.StoreSpotter.util.CookieUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.sojoo.StoreSpotter.util.CookieUtil.addCookie;

@Component
public class TokenProvider implements InitializingBean {
    private final RedisService redisService;
    private final CookieUtil cookieUtil;
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private Key key;
    private final static int COOKIE_EXPIRE_SECONDS = 3600;      // 쿠키 존재 시간 1시간 설정

    public TokenProvider(
            RedisService redisService, CookieUtil cookieUtil, @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessTokenExpiration}") long accessTokenExpiration,
            @Value("${jwt.refreshTokenExpiration}") long refreshTokenExpiration) {
        this.redisService = redisService;
        this.cookieUtil = cookieUtil;
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        System.out.println("TokenProvider createAccessToken 실행");
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();

        // Access Token
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        System.out.println("TokenProvider createRefreshToken 실행");
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        System.out.println("TokenProvider getAuthentication ");
        Claims claims = Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        System.out.println("getAuthentication : " + principal);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    public boolean validateToken(String token, HttpServletResponse response) {
        System.out.println("TokenProvider validateToken 실행");
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            Authentication authentication = getAuthentication(token);
            String refreshToken = redisService.getValues(token);
            if (validRefreshToken(refreshToken)) {
                String accessToken = createAccessToken(authentication);
                addCookie(response, "access_token", accessToken, COOKIE_EXPIRE_SECONDS);
                redisService.changeValues(accessToken, refreshToken);
            }
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public boolean validRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getUsernameFromToken(String accessToken){
        Claims claims = Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.getSubject();
    }
}