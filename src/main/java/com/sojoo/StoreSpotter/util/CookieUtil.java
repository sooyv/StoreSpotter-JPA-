package com.sojoo.StoreSpotter.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String accessToken, String value, int maxAge) {
        Cookie cookie = new Cookie(accessToken, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void delCookie(HttpServletResponse response) {
        Cookie myCookie = new Cookie("access_token", null);
        myCookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        myCookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(myCookie);
    }
}
