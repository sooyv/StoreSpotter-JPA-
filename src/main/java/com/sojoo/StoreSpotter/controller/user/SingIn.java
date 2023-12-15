package com.sojoo.StoreSpotter.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SingIn {

    // 로그인
    @GetMapping("/signin")
    public ModelAndView signIn() {
        return new ModelAndView("/signIn-signUp/signIn");
    }
}
