package com.sojoo.StoreSpotter.jwt.jwt;

import com.sojoo.StoreSpotter.service.redis.RedisService;
import com.sojoo.StoreSpotter.service.user.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    private final CustomUserDetailsService customUserDetailsService;

    private Key key;
    private final static int COOKIE_EXPIRE_SECONDS = 3600;      // 쿠키 존재 시간 1시간 설정

    public TokenProvider(
            RedisService redisService, @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessTokenExpiration}") long accessTokenExpiration,
            @Value("${jwt.refreshTokenExpiration}") long refreshTokenExpiration, CustomUserDetailsService customUserDetailsService) {
        this.redisService = redisService;
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * create AccessToken, RefreshToken
     */
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

    // AccessToken 이 만료되었을 때 newAccessToken 발급 및 Cookie 및 Redis 에 반영
    public String reissueAccessToken(String accessToken, HttpServletResponse response){
        String username = getUsernameFromToken(accessToken);
        String refreshToken = redisService.getValues(username);

        if (validRefreshToken(refreshToken)) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            String newAccessToken = createAccessToken(authenticationToken);
            addCookie(response, "access_token", newAccessToken, COOKIE_EXPIRE_SECONDS);
            redisService.changeValues(getUsernameFromToken(newAccessToken), refreshToken);

            return newAccessToken;
        }
        return accessToken;
    }

    /**
     * validate AccessToken, RefreshToken
     */
    public boolean validateToken(String token) {
        System.out.println("TokenProvider validateToken 실행");
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("A잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("A만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("A지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("AJWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public boolean validRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("R잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("R만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("R지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("RJWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


    /**
     * get info from accessToken
     */

    public Claims getClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        System.out.println("TokenProvider getAuthentication ");
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        System.out.println("getAuthentication : " + principal);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Date getExpiredFromToken(String accessToken) {
        try {
            Claims claims = getClaims(accessToken);
            return claims.getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        } catch (Exception e) {
            // 다른 종류의 예외 처리
            return null;
        }
    }

    public String getUsernameFromToken(String accessToken) {
        try {
            Claims claims = getClaims(accessToken);
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            // 다른 종류의 예외 처리
            return null;
        }
    }

    public boolean mvcIntercepterValid(String accessToken){
        String username = getUsernameFromToken(accessToken);
        String refreshToken = redisService.getValues(username);

        if (refreshToken == null){
            return false;
        } else{
            return validRefreshToken(refreshToken);
        }
    }
}