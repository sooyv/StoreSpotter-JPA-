//package com.sojoo.StoreSpotter.config;
//
//import com.sojoo.StoreSpotter.config.jwt.JwtTokenProvider;
//import com.sojoo.StoreSpotter.config.jwt.TokenAuthenticationFilter;
//import com.sojoo.StoreSpotter.service.Member.UserDetailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final UserDetailService memberService;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return ((web) -> web.ignoring()
//                .antMatchers("/css/**", "/js/**"));
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        // token을 사용하는 방식이기 때문에 csrf를 disable
//        http.csrf().disable();
//
//        http.authorizeRequests()
//                // main, login 페이지, login 프로세스, 회원가입 페이지, 회원가입 프로세스, 이메일 중복체크 ajax, JWT token 발급, 평균 거리 검색 ajax
//                .antMatchers("/", "/login", "/signup", "/member/signup", "/signup/checkid", "/avg-dist").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated();   // 그 외 인증 없이 차단
//
//        http.formLogin()
//                .loginPage("/login")               // 로그인 설정
//                .usernameParameter("email")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/")
//                .and()
//                .logout()   // 로그아웃 설정
//                    .logoutSuccessUrl("/login");
//
//        // 세션 사용 안함
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailService userDetailService) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(memberService)
//                .passwordEncoder(bCryptPasswordEncoder)
//                .and()
//                .build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
