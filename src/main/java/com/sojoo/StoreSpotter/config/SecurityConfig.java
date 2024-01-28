package com.sojoo.StoreSpotter.config;

import com.sojoo.StoreSpotter.jwt.config.JwtTokenProvider;
import com.sojoo.StoreSpotter.jwt.config.TokenAuthenticationFilter;
import com.sojoo.StoreSpotter.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.sojoo.StoreSpotter.config.oauth.OAuth2SuccessHandler;
import com.sojoo.StoreSpotter.config.oauth.OAuth2UserCustomService;
import com.sojoo.StoreSpotter.jwt.jwtRepository.RefreshTokenRepository;
import com.sojoo.StoreSpotter.service.member.MemberService;
import com.sojoo.StoreSpotter.service.member.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final UserDetailService memberService;
    private final MemberService memberServiceOauth;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public WebSecurityCustomizer configure() {
        return ((web) -> web.ignoring()
                .antMatchers("/css/**", "/js/**"));
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // token을 사용하는 방식이기 때문에 csrf를 disable
        http.csrf().disable()
        // 세션 사용 안함
        .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                // main, login 페이지, login 프로세스, 회원가입 페이지, 회원가입 프로세스, 이메일 중복체크 ajax, JWT token 발급, 평균 거리 검색 ajax
                .antMatchers("/", "/login", "/signup","/member/login", "/member/signup", "/signup/checkid", "/avg-dist", "/api/token").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated();   // 그 외 인증 없이 차단 - 일시 수정
//                .anyRequest().permitAll();   // 일시 허용

        http.addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        http.formLogin()
//                .loginPage("/login")               // 로그인 설정
//                .loginProcessingUrl("/member/login")
//                .defaultSuccessUrl("/")
//                .and()
//                .logout()   // 로그아웃 설정
//                .logoutSuccessUrl("/login");

        http.oauth2Login()
                .loginPage("/login")
                .loginPage("/signup")
                .authorizationEndpoint()
                // Authorization 요청과 관련된 상태 저장
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(oAuth2SuccessHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(memberService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(jwtTokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), memberServiceOauth);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
