package com.sojoo.StoreSpotter.jwt.controller;

import com.sojoo.StoreSpotter.jwt.dto.UserDto;
import com.sojoo.StoreSpotter.jwt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signup(UserDto userDto,
                                         @RequestParam("userName") String userName,
                                         @RequestParam("nickName") String nickName,
                                         @RequestParam("password") String password) {
        userDto.setUsername(userName);
        userDto.setNickname(nickName);
        userDto.setPassword(password);
        System.out.println(userDto);
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