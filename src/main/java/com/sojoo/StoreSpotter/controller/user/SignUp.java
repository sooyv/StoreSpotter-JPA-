package com.sojoo.StoreSpotter.controller.user;

import com.sojoo.StoreSpotter.controller.form.memberForm;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//@RestController
@Controller
public class SignUp {
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

        if (name == null || name == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (email == null || email == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (password == null || password == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (checkPassword == null || checkPassword == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (phone == null || phone == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 일치
        if (!password.equals(checkPassword)) {
            return new ResponseEntity<>("notEqualPassword", HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 정규식
        String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        if (!password.matches(pwRegExp)) {
            return new ResponseEntity<>("passwordRegExp", HttpStatus.BAD_REQUEST);
        }


        System.out.println(name);
        System.out.println(email);
        System.out.println(password);
        System.out.println(checkPassword);
        System.out.println(phone);


        return new ResponseEntity<>("sign up success", HttpStatus.OK);

    }

}
