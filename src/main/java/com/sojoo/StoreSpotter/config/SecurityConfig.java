package com.sojoo.StoreSpotter.config;

import com.sojoo.StoreSpotter.interceptor.MvcInterceptor;
import com.sojoo.StoreSpotter.jwt.handler.JwtAccessDeniedHandler;
import com.sojoo.StoreSpotter.jwt.handler.JwtAuthenticationEntryPoint;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final MvcInterceptor mvcInterceptor;


    @Bean
    public WebSecurityCustomizer configure() {
        return ((web) -> web.ignoring()
                .antMatchers("/css/**", "/js/**"));
    }
    public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler, MvcInterceptor mvcInterceptor) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.mvcInterceptor = mvcInterceptor;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // mvc intercepter 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mvcInterceptor);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                        .authorizeHttpRequests()

                // main, login 페이지, login 프로세스, 회원가입 페이지, 회원가입 프로세스, 이메일 중복체크 ajax, JWT token 발급, 평균 거리 검색 ajax
                .antMatchers("/", "/login", "/user/auth/logout", "/signup", "/user/auth/login", "/user/auth/signup", "/signup/checkid",
                        "/user/find-account", "/user/account", "/user/password", "/avg-dist", "/search/recommend", "/favicon.ico",
                "/signup/mail-code", "/user/password", "/user/account").permitAll()

                .antMatchers("/user", "/admin", "/mypage", "/mypage/**").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()   // 그 외 인증 없이 차단 - 일시 수정);
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        http.exceptionHandling().accessDeniedPage("/")        // 403 발생시 main 페이지로 이동
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()); // 401 오류 발생 시 처리 클래스 지정


        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
}