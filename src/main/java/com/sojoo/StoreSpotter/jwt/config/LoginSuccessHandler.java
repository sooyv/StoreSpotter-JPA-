package com.sojoo.StoreSpotter.jwt.config;

import com.sojoo.StoreSpotter.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.jwt.RefreshToken;
import com.sojoo.StoreSpotter.jwt.jwtRepository.RefreshTokenRepository;
import com.sojoo.StoreSpotter.service.member.MemberService;
import com.sojoo.StoreSpotter.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECH_PATH = "/";

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("LoginSuccessHandler onAuthenticationSuccess ");
        // Principal을 통해 사용자 정보에 접근합니다.
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            System.out.println("principal : " + principal);

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Member member = memberService.findByEmail(userDetails.getUsername());
                System.out.println("principal instanceof UserDetails : " + member);

                // 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
                String refreshToken = jwtTokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
                saveRefreshToken(member.getMemberId(), refreshToken);
                addRefreshTokenToCookie(request, response, refreshToken);

                // 액세스 토큰 생성 -> 패스에 액세스 토큰 추가
                String accessToken = jwtTokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
                String targetUrl = getTargetUrl(accessToken);

                // 인증 관련 설정값, 쿠키 제거
//                clearAuthenticationAttributes(request, response);

                // 리다이렉트
                getRedirectStrategy().sendRedirect(request, response, targetUrl);

                // userDetails에서 필요한 정보 추출
                String username = userDetails.getUsername();
                System.out.println("getPrinciple : " + username);
                // ... 다른 정보 추출
            }
        }
    }

    // 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        System.out.println("OAuth2SuccessHandler saveRefreshToken");
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        System.out.println("OAuth2SuccessHandler addRefreshTokenToCookie ");
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 관련 설정값, 쿠키 제거
//    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        super.clearAuthenticationAttributes(request);
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//    }

    // 액세스 토큰을 패스에 추가
    private String getTargetUrl(String token) {
        System.out.println("OAuth2SuccessHandler getTargetUrl");
        return UriComponentsBuilder.fromUriString(REDIRECH_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
