package com.sojoo.StoreSpotter.controller.Member;

import com.sojoo.StoreSpotter.config.jwt.JwtTokenProvider;
//import com.sojoo.StoreSpotter.controller.form.memberForm;
import com.sojoo.StoreSpotter.dto.Member.Member;
import com.sojoo.StoreSpotter.dto.Member.SignupDto;
import com.sojoo.StoreSpotter.repository.Member.MemberRepository;
import com.sojoo.StoreSpotter.service.Member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원가입 page
//    @GetMapping("/signup")
//    public String signUpPage() {
//        return "/loginSignUp/signUp";
//    }

    @GetMapping("/signup")
    public ModelAndView signUpPage(Model signUpDto) {
        signUpDto.addAttribute("signUpDto", new SignupDto());
        return new ModelAndView("/loginSignUp/signUp");
    }

//    @GetMapping("/signup")
//    public ModelAndView signUpPage(Model si) {
//        memberForm.addAttribute("memberForm", new memberForm());
//        return new ModelAndView("/loginSignUp/signUp");
//    }

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
    @Transactional
    @PostMapping("/member/signup")
    public ResponseEntity<String> signUp(SignupDto signupDto) {

        String name = signupDto.getName();
        String email = signupDto.getEmail();
        String password = signupDto.getPassword();
        String checkPassword = signupDto.getCheckPassword();
        String phone = signupDto.getPhone();

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

        memberService.joinMember(member);

        return new ResponseEntity<>("Successfully sign-up", HttpStatus.OK);
    }


    // 로그인 springsecurity + jwt
//    @PostMapping("/member/login")
//    public String login(@RequestBody Map<String, String> Member) {
////        log.info("user email = {}", member.get("email"));
//        Member member = memberRepository.findByMemberEmail(member.getMemberEmail())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
//
//        return jwtTokenProvider.generateToken(member.getUsername());
//    }




    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView signIn() {
        return new ModelAndView("loginSignUp/login");
    }


    @GetMapping("/signInfo")
    public ModelAndView signInfo() {
        return new ModelAndView("/loginSignUp/findSignInfo");
    }
}
