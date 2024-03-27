package com.sojoo.StoreSpotter.controller.user;

import com.sojoo.StoreSpotter.common.exception.SmtpSendFailedException;
import com.sojoo.StoreSpotter.common.exception.UserNotFoundException;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.service.mail.MailService;
import com.sojoo.StoreSpotter.service.user.UserInfoService;
import com.sojoo.StoreSpotter.service.user.UserValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class UserController {
    private final MailService mailService;
    private final UserValidateService userValidateService;
    private final UserInfoService userInfoService;

    public UserController(MailService mailService, UserValidateService userValidateService, UserInfoService userInfoService) {
        this.mailService = mailService;
        this.userValidateService = userValidateService;
        this.userInfoService = userInfoService;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView login() {
        System.out.println("login 실행");
        return new ModelAndView("loginSignUp/login");
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public ModelAndView signup(Model model) {
//        model.addAttribute("userDto", new UserDto());
        return new ModelAndView("loginSignUp/signUp");
    }


    // 회원가입 메일 인증
    @PostMapping("/signup/mail-code")
    public ResponseEntity<String> sendEmailCode(@RequestParam String email) {

        try {
            // 메일 중복확인
            ResponseEntity<String> checkDuplicateEmail = userValidateService.checkDuplicateEmail(email);
            if (checkDuplicateEmail.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return checkDuplicateEmail;
            }

            mailService.sendCertificationMail(email);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (SmtpSendFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 회원정보 찾기 페이지
    @GetMapping("/user/find-account")
    public ModelAndView findUserInfo() {
        return new ModelAndView("loginSignUp/findUserInfo");
    }

    // 이메일 찾기
    @PostMapping("/user/account")
    public List<String> findUserAccount(@RequestParam("username") String username, @RequestParam("phone") String phone) {

        List<String> userEmail = userInfoService.findUserEmail(username, phone);
        return userEmail;
    }

    // 비밀번호 재발급
    @PostMapping("/user/password")
    public ResponseEntity<String> reissuePassword(@RequestParam String email) throws UserNotFoundException {
        try {
            userInfoService.updateUserPw(email);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("FailedUpdatePassword", HttpStatus.BAD_REQUEST);
        }
    }

}