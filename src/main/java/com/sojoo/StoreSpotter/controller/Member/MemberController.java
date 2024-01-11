package com.sojoo.StoreSpotter.controller.Member;

//import com.sojoo.StoreSpotter.controller.form.memberForm;
import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.dto.member.MemberDto;
        import com.sojoo.StoreSpotter.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        signUpDto.addAttribute("memberDto", new MemberDto());
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
    public ResponseEntity<String> signUp(MemberDto memberDto) {

        String name = memberDto.getName();
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();
        String checkPassword = memberDto.getCheckPassword();
        String phone = memberDto.getPhone();

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


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/signInfo")
    public ModelAndView signInfo() {
        return new ModelAndView("/loginSignUp/findSignInfo");
    }
}
