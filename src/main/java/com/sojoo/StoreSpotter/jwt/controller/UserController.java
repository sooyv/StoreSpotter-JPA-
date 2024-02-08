package com.sojoo.StoreSpotter.jwt.controller;

import com.sojoo.StoreSpotter.jwt.dto.UserDto;
import com.sojoo.StoreSpotter.jwt.service.UserService;
import com.sojoo.StoreSpotter.jwt.service.UserValidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final UserValidateService userValidateService;

    public UserController(UserService userService, UserValidateService userValidateService) {
        this.userService = userService;
        this.userValidateService = userValidateService;
    }

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

    // 회원가입 페이지
    @GetMapping("/signup")
    public ModelAndView signUpPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return new ModelAndView("/loginSignUp/signUp");
    }

    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("loginSignUp/login");
    }

}