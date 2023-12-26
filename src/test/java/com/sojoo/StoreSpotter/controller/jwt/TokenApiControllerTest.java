//package com.sojoo.StoreSpotter.controller.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sojoo.StoreSpotter.config.jwt.JwtFactory;
//import com.sojoo.StoreSpotter.config.jwt.JwtProperties;
//import com.sojoo.StoreSpotter.dto.Member.Member;
//import com.sojoo.StoreSpotter.dto.jwt.CreateAccessTokenRequest;
//import com.sojoo.StoreSpotter.dto.jwt.RefreshToken;
//import com.sojoo.StoreSpotter.repository.Member.MemberRepository;
//import com.sojoo.StoreSpotter.repository.jwt.RefreshTokenRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class TokenApiControllerTest {
//
//    @Autowired
//    protected MockMvc mockMvc;
//    @Autowired
//    protected ObjectMapper objectMapper;
//    @Autowired
//    private WebApplicationContext context;
//    @Autowired
//    JwtProperties jwtProperties;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();
//        memberRepository.deleteAll();
//    }
//
//    @DisplayName("crateNewAccessToken: 새로운 엑세스 토큰을 발급한다.")
//    @Test
//    public void createNewAccessToken() throws Exception {
//
//        // given
//        final String url = "/api/token";
//
//        Member testMember = memberRepository.save(User.builder()
//                .username("user@gmail.com")
//                .password("test")
//                .build());
//
//
//        String refreshToken = JwtFactory.builder()
//                .claims(Map.of("id", testMember.getMemberId()))
//                .build()
//                .createToken(jwtProperties);
//
//        refreshTokenRepository.save(new RefreshToken(testMember.getMemberId(), refreshToken));
//
//        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
//        request.setRefreshToken(refreshToken);
//        final String requestBody = objectMapper.writeValueAsString(request);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));
//
//        // then
//        resultActions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty());
//    }
//
//}