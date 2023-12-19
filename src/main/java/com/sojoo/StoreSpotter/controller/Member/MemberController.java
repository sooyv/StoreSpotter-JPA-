package com.sojoo.StoreSpotter.controller.Member;

import com.sojoo.StoreSpotter.controller.form.memberForm;
import com.sojoo.StoreSpotter.dto.Member.Member;
import com.sojoo.StoreSpotter.service.Member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;


    // 회원가입 page
    @GetMapping("/signup")
    public ModelAndView signUpPage(Model memberForm) {
        memberForm.addAttribute("memberForm", new memberForm());
        return new ModelAndView("/loginSignUp/signUp");
    }

    // 회원가입 이메일 중복 검사
    @PostMapping("/signup/checkid")
    @ResponseBody
    public int checkid(@RequestParam("id") String id, @RequestParam("type") String type) {
        String result = memberService.checkId(id, type);
        if(result != null && result.equals("0")) {
            return 0;
        } else {
            return 1;
        }
    }

    // 회원가입
    @PostMapping("/member/signup")
    public ResponseEntity<String> signUp(memberForm memberInfo) {

        String name = memberInfo.getName();
        String email = memberInfo.getEmail();
        String password = memberInfo.getPassword();
        String checkPassword = memberInfo.getCheckPassword();
        String phone = memberInfo.getPhone();

        // 모든 항목 입력 검사
        ResponseEntity<String> notNullMemberInfo = memberService.notNullMemberInfo(name, email, password, checkPassword, phone);
        if (notNullMemberInfo != null) {
            return notNullMemberInfo;
        }

        // 비밀번호 일치 검사
        ResponseEntity<String> checkEqualPassword = memberService.checkEqualPassword(password, checkPassword);
        if (checkEqualPassword != null) {
            return checkEqualPassword;
        }

        // 비밀번호 정규식 검사
        ResponseEntity<String> passwordRegExp = memberService.passwordRegExp(password);
        if (passwordRegExp != null) {
            return passwordRegExp;
        }


        Member member = new Member();
        member.setMemberName(name);
        member.setMemberEmail(email);
        member.setMemberPassword(password);
        member.setMemberPhone(phone);

//        memberService.join(member);
        // 이메일 중복 검사
        ResponseEntity<String> validateDuplicateMember = memberService.join(member);
        if (validateDuplicateMember != null) {
            return validateDuplicateMember;
        }

        return new ResponseEntity<>("sign up success", HttpStatus.OK);
    }



    // 로그인
    @GetMapping("/login")
    public ModelAndView signIn() {
        return new ModelAndView("loginSignUp/login");
    }


    @GetMapping("/signInfo")
    public ModelAndView signInfo() {
        return new ModelAndView("/loginSignUp/findSignInfo");
    }
}
