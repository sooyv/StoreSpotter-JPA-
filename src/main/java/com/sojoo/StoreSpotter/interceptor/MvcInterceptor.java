package com.sojoo.StoreSpotter.interceptor;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class MvcInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public MvcInterceptor(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView == null) {
            return;
        }

        Cookie accessTokenCookie = CookieUtil.getCookie(request, "access_token");
        if (accessTokenCookie == null) {
            return;
        }

        String accessToken = accessTokenCookie.getValue();
        String username = tokenProvider.getUsernameFromToken(accessToken);
        if (!tokenProvider.validRefreshTokenFromAccessToken(accessToken)) {
            return;
        }

        Optional<User> user = userRepository.findByUsername(username);
        user.ifPresent(u -> modelAndView.addObject("userNickname", u.getNickname()));


    }
}