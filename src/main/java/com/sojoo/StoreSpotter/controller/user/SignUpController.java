package com.sojoo.StoreSpotter.controller.user;

import com.sojoo.StoreSpotter.controller.form.memberForm;
//import com.sojoo.StoreSpotter.service.user.SignUpService;
import com.sojoo.StoreSpotter.service.user.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//@RestController
@Controller
public class SignUpController {

    @Autowired
    private SignUpService signUpService;


    // 회원가입 page
    @GetMapping("/signup")
    public ModelAndView signUpPage(Model member) {
        member.addAttribute("member", new memberForm());
        return new ModelAndView("/signIn-signUp/signUp");
    }

    // 회원가입
    @PostMapping("/member/signup")
    public ResponseEntity<String> signUp(memberForm member) {

        String name = member.getName();
        String email = member.getEmail();
        String password = member.getPassword();
        String checkPassword = member.getCheckPassword();
        String phone = member.getPhone();

        // 모든 항목 입력 검사
        ResponseEntity<String> notNullMemberInfo = signUpService.notNullMemberInfo(name, email, password, checkPassword, phone);
        if (notNullMemberInfo != null) {
            return notNullMemberInfo;
        }

//        // 비밀번호 일치 검사
        ResponseEntity<String> checkEqualPassword = signUpService.checkEqualPassword(password, checkPassword);
        if (checkEqualPassword != null) {
            return checkEqualPassword;
        }


//        // 비밀번호 정규식 검사
        ResponseEntity<String> passwordRegExp = signUpService.passwordRegExp(password);
        if (passwordRegExp != null) {
            return passwordRegExp;
        }

        return new ResponseEntity<>("sign up success", HttpStatus.OK);
    }
}
