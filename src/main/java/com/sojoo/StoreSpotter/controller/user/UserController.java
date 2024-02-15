package com.sojoo.StoreSpotter.controller.user;

import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.entity.Member.User;
import com.sojoo.StoreSpotter.service.mail.MailService;
import com.sojoo.StoreSpotter.service.user.UserService;
import com.sojoo.StoreSpotter.service.user.UserValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final MailService mailService;
    private final UserValidateService userValidateService;

    public UserController(UserService userService, MailService mailService, UserValidateService userValidateService) {
        this.userService = userService;
        this.mailService = mailService;
        this.userValidateService = userValidateService;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("loginSignUp/login");
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public ModelAndView signUpPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return new ModelAndView("/loginSignUp/signUp");
    }

    // 회원가입
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
        System.out.println(userDto);

        // 이메일 중복 검사
        ResponseEntity<String> checkDuplicateEmail = userValidateService.checkDuplicateEmail(userDto);
        if (checkDuplicateEmail != null) {
            return checkDuplicateEmail;
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

        userService.signup(userDto);
        return new ResponseEntity<>("Successfully sign-up", HttpStatus.OK);    }

    // 회원가입 메일 인증
    @PostMapping("/signup/mail-code")
    public ResponseEntity<String> sendEmailCode(@RequestParam String email) throws MessagingException, IllegalStateException {

        if (email == null) {
            return ResponseEntity.badRequest().body("인증 메일 null");
        }

        // 메일 정규화 확인 추가

        try {
            String code = mailService.sendCertificationMail(email);
            return ResponseEntity.ok(code);
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }



    // 회원정보 찾기
    @GetMapping("/find-user")
    public ModelAndView findUserInfo() {
        return new ModelAndView("/loginSignUp/findUserInfo");
    }

    // 비밀번호 재발급
    @PostMapping("/user/password")
    public ResponseEntity<String> reissuePassword(@RequestParam String email) throws NoSuchElementException {
        try {
            String reissuePasswordSuccess = mailService.updateUserPw(email);
            return ResponseEntity.ok(reissuePasswordSuccess);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("등록되지 않은 이메일 주소입니다. 다시 입력해주세요.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("비밀번호 재설정에 실패했습니다. 다시 시도해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

}