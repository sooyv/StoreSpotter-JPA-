package com.sojoo.StoreSpotter.config.intercepter;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.ShiftRight;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
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

    private final CookieUtil cookieUtil;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public MvcInterceptor(CookieUtil cookieUtil, TokenProvider tokenProvider, UserRepository userRepository) {
        this.cookieUtil = cookieUtil;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("preHandle 실행");
//        return true;
//    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러 실행 후, View 렌더링 전에 수행되는 코드
        System.out.println("postHandle 실행");

        if (modelAndView != null) { // modelAndView 객체가 null이 아닌 경우에만 처리
            // 쿠키 받아오기
            Cookie cookie = cookieUtil.getCookie(request, "access_token");
            // System.out.println("postHandle 쿠키 확인 : " + cookie);

            if (cookie != null) {
                // 쿠키의 토큰 확인하여 user 정보 추출
                String accessToken = cookie.getValue();
                String username = tokenProvider.getUsernameFromToken(accessToken);

                // 유저 존재 여부 확인
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    String userNickname = user.get().getNickname();
                    String signOrMypage = "마이페이지";
                    String loginOrLogout = "로그아웃";

                    // 모델에 데이터 추가
                    modelAndView.addObject("userNickname", userNickname);
                    modelAndView.addObject("signOrMypage", signOrMypage);
                    modelAndView.addObject("loginOrLogout", loginOrLogout);
                } else {
                    log.info("user is null");

                    String signOrMypage = "회원가입";
                    String loginOrLogout = "로그인";

                    modelAndView.addObject("loginOrLogout", loginOrLogout);
                    modelAndView.addObject("signOrMypage", signOrMypage);
                }
            } else {
                log.info("cookie is null");

                String signOrMypage = "회원가입";
                String loginOrLogout = "로그인";

                modelAndView.addObject("loginOrLogout", loginOrLogout);
                modelAndView.addObject("signOrMypage", signOrMypage);
            }
        }
    }

//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 컨트롤러 실행 후, View 렌더링 전에 수행되는 코드
//        System.out.println("postHandle 실행");
//
//        // 쿠키 받아오기
//        Cookie cookie = cookieUtil.getCookie(request, "access_token");
//        // System.out.println("postHandle 쿠키 확인 : " + cookie);
//
//        if (cookie != null) {
//            // 쿠키의 토큰 확인하여 user 정보 추출
//            String accessToken = cookie.getValue();
//            String username = tokenProvider.getUsernameFromToken(accessToken);
//
//            // 유저 존재 여부 확인
//            Optional<User> user = userRepository.findByUsername(username);
//            if (!user.isEmpty()) {
////                String userNickname = user.get().getNickname();
//                String signOrMypage = "마이페이지";
//                String loginOrLogout = "로그아웃";
//
//                // 모델에 데이터 추가
////                modelAndView.addObject("username", userNickname);
//                modelAndView.addObject("signOrMypage", signOrMypage);
//                modelAndView.addObject("loginOrLogout", loginOrLogout);
//
//            } else {
//                log.info("user is null");
//                String userNickname = "";
//                String signOrMypage = "회원가입";
//                String loginOrLogout = "로그인";
//
//                modelAndView.addObject("username", userNickname);
//                modelAndView.addObject("loginOrLogout", loginOrLogout);
//                modelAndView.addObject("signOrMypage", signOrMypage);
//            }
//
//
//        } else {
//            log.info("cookie is null");
//
//            String signOrMypage = "회원가입";
//            String loginOrLogout = "로그인";
//
////            modelAndView.addObject("username", userNickname);
//            modelAndView.addObject("loginOrLogout", loginOrLogout);
//            modelAndView.addObject("signOrMypage", signOrMypage);
////            throw new NullPointerException();
//        }
//
//    }
}
