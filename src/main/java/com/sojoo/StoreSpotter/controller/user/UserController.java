package com.sojoo.StoreSpotter.controller.user;

import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.service.mail.MailService;
import com.sojoo.StoreSpotter.service.user.UserInfoService;
import com.sojoo.StoreSpotter.service.user.UserService;
import com.sojoo.StoreSpotter.service.user.UserValidateService;
import com.sojoo.StoreSpotter.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final UserValidateService userValidateService;
    private final UserInfoService userInfoService;
    private final CookieUtil cookieUtil;

    public UserController(UserService userService, MailService mailService, UserValidateService userValidateService, UserInfoService userInfoService, CookieUtil cookieUtil) {
        this.userService = userService;
        this.mailService = mailService;
        this.userValidateService = userValidateService;
        this.userInfoService = userInfoService;
        this.cookieUtil = cookieUtil;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView login() {
        System.out.println("login 실행");
        return new ModelAndView("loginSignUp/login");
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public ModelAndView signUpPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return new ModelAndView("loginSignUp/signUp");
    }



    // 회원가입
    @Transactional
    @PostMapping("/member/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
        System.out.println(userDto);

        // 이메일 중복 검사
        ResponseEntity<String> checkDuplicateEmail = userValidateService.checkDuplicateEmail(userDto);
        if (checkDuplicateEmail != null) {
            return checkDuplicateEmail;
        }

        // 이메일 코드 검사
        String checkMailCodeResult = userValidateService.checkMailCode(userDto);

        if (checkMailCodeResult != null && "notEqualMailCode".equals(checkMailCodeResult)) {
            return new ResponseEntity<>("notEqualMailCode", HttpStatus.BAD_REQUEST);
        } else if (checkMailCodeResult != null && "expirationMailCode".equals(checkMailCodeResult)) {
            return new ResponseEntity<>("expirationMailCode", HttpStatus.BAD_REQUEST);
        }


        // 모든 항목 입력 검사
        ResponseEntity<String> notNullMemberInfo = userValidateService.notNullMemberInfo(userDto);
        if (notNullMemberInfo != null) {
            return notNullMemberInfo;
        }

        // 비밀번호 일치 검사
        ResponseEntity<String> checkEqualPassword = userValidateService.checkEqualPassword(userDto);
        if (checkEqualPassword != null) {
            return checkEqualPassword;
        }

        // 비밀번호 정규식 검사
        ResponseEntity<String> passwordRegExp = userValidateService.passwordRegExp(userDto);
        if (passwordRegExp != null) {
            return passwordRegExp;
        }

        ResponseEntity<String> phoneRegExp = userValidateService.phoneRegExp(userDto);
        if (phoneRegExp != null) {
            return phoneRegExp;
        }

        userService.signup(userDto);
        return new ResponseEntity<>("Successfully sign-up", HttpStatus.OK);    }


    // 회원가입 메일 인증
    @PostMapping("/signup/mail-code")
    public ResponseEntity<String> sendEmailCode(@RequestParam String email) throws MessagingException, IllegalStateException {
        System.out.println("sendEmailCode 이메일 확인 : " + email);

        if (email == null) {
            return ResponseEntity.badRequest().body("인증 메일 null");
        }

        // 메일 정규화 확인 추가

        try {
//            String code = mailService.sendCertificationMail(email);
//            return ResponseEntity.ok();
            mailService.sendCertificationMail(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("인증메일 전송 실패");
        }
    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }



    // 회원정보 찾기
    @GetMapping("/find-user")
    public ModelAndView findUserInfo() {
        return new ModelAndView("loginSignUp/findUserInfo");
    }

    // 이메일 찾기
    @PostMapping("/user/account")
    public List<String> findUserId(@RequestParam("username") String username, @RequestParam("phone") String phone) {

        List<String> userEmail = userInfoService.findUserEmail(username, phone);
        return userEmail;
    }


    // 비밀번호 재발급
    @PostMapping("/user/password")
    public ResponseEntity<String> reissuePassword(@RequestParam String email) throws NoSuchElementException {
        try {
            String reissuePasswordSuccess = userInfoService.updateUserPw(email);
            System.out.println("reissuePassword email  : " + email);
            return ResponseEntity.ok(reissuePasswordSuccess);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("notExistEmail", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("FailedUpdatePassword", HttpStatus.BAD_REQUEST);
        }
    }

}