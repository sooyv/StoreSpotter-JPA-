//package com.sojoo.StoreSpotter.config.configu.jwt;
//
//import com.sojoo.StoreSpotter.config.jwt.JwtProperties;
//import com.sojoo.StoreSpotter.config.jwt.JwtTokenProvider;
//import com.sojoo.StoreSpotter.dto.Member.Member;
//import com.sojoo.StoreSpotter.repository.Member.MemberRepository;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.test.web.reactive.server.JsonPathAssertions;
//
//import java.time.Duration;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class JwtTokenProviderTest {
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private JwtProperties jwtProperties;
//
//
//    @DisplayName("generatedToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있음")
//    @Test
//    void generateToken() {
//        // given
//        Member testMember = memberRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        // when
//        String token = jwtTokenProvider.generateToken(testMember, Duration.ofDays(14));
//
//        // then
//        Long userId = Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody()
//                .get("id", Long.class);
//
//        assertThat(userId).isEqualTo(testMember.getMemberId());
//    }
//
//    @DisplayName("valideToken(): 유효한 토큰일때에 유효성 검증에 성공")
//    @Test
//    void validToken_valideToken() {
//        // given
//        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
//
//        // when
//        boolean result = jwtTokenProvider.validToken(token);
//
//        // then
//        assertThat(result).isTrue();
//    }
//
//    @DisplayName(("getAuthenication(): 토큰 기반으로 인증 정보를 가져올 수 있다."))
//    @Test
//    void getAuthentication() {
//        // given
//        String userEmail = "user@email.com";
//        String token = JwtFactory.builder()
//                .subject(userEmail)
//                .build()
//                .createToken(jwtProperties);
//
//        // when
//        Authentication authentication = jwtTokenProvider.getAuthentication(token);
//
//        // then
//        assertThat(((UserDetails) authentication.getPrincipal()).getUsername().isEqualTo(userEmail));
//    }
//
//    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
//    @Test
//    void getUserId() {
//        // given
//        Long userId = 1L;
//        String token = JwtFactory.builder()
//                .claims(Map.of("id", userId))
//                .build()
//                .createToken(jwtProperties);
//
//        // when
//        Long userIdByToken = jwtTokenProvider.getUserEmail(token);
//
//        // then
//        assertThat(userIdByToken).isEqualTo(userId);
//    }
//
//}