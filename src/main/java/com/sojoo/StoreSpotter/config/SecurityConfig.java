package com.sojoo.StoreSpotter.config;

import com.sojoo.StoreSpotter.service.Member.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailService memberService;

    @Bean
    public WebSecurityCustomizer configure() {
        return ((web) -> web.ignoring()
                .antMatchers("/css/**", "/js/**"));
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                // "/signup/checkid" : 이메일 중복체크 ajax
                .antMatchers("/", "/login", "/member/login", "/signup", "/member/signup", "/signup/checkid").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")               // 로그인 설정
                .loginProcessingUrl("/member/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()                           // 로그아웃 설정
                .logoutSuccessUrl("/login");


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
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
