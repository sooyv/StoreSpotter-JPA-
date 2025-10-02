package com.sojoo.StoreSpotter.controller.user;

//import com.sojoo.StoreSpotter.api.UserApi;
import com.sojoo.StoreSpotter.api.ErrorApiResponses;
import com.sojoo.StoreSpotter.api.MgrSwaggerDoc;
import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.error.ErrorResponse;
import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
import com.sojoo.StoreSpotter.service.mail.MailService;
import com.sojoo.StoreSpotter.service.user.UserInfoService;
import com.sojoo.StoreSpotter.service.user.UserValidateService;
import io.swagger.v3.oas.annotations.Operation;
import com.sojoo.StoreSpotter.api.ApiResult;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(MailService mailService, UserValidateService userValidateService, UserInfoService userInfoService) {
        this.mailService = mailService;
        this.userValidateService = userValidateService;
        this.userInfoService = userInfoService;
    }

    // 로그인 페이지
    @TimeTrace
    @Operation(summary = MgrSwaggerDoc.User.Api.page.LOGIN_SUMMARY, description = MgrSwaggerDoc.User.Api.page.LOGIN_DESC)
    @ApiResponse(responseCode = MgrSwaggerDoc.Response.Success.Code, description = MgrSwaggerDoc.Response.Success.Desc)
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("loginSignUp/login");
    }


    // 회원가입 페이지
    @Operation(summary = MgrSwaggerDoc.User.Api.page.SIGNUP_SUMMARY, description = MgrSwaggerDoc.User.Api.page.SIGNUP_DESC)
    @ApiResponse(responseCode = MgrSwaggerDoc.Response.Success.Code, description = MgrSwaggerDoc.Response.Success.Desc)
    @GetMapping("/signup")
    public ModelAndView signup(Model model) {
//        model.addAttribute("userDto", new UserDto());
        return new ModelAndView("loginSignUp/signUp");
    }


    // 회원가입 메일 인증
    @Operation(summary = MgrSwaggerDoc.User.Api.sendMailCode.Summary, description = MgrSwaggerDoc.User.Api.sendMailCode.Desc)
    @PostMapping("/signup/mail-code")
    @ApiResult(
	errors = { ErrorCode.EMAIL_DUPLICATION, ErrorCode.SMTP_SEND_FAILED })
    public ResponseEntity<Void> sendEmailCode(@RequestParam String email) {

        // 메일 중복확인
//            ResponseEntity<String> checkDuplicateEmail = userValidateService.checkDuplicateEmail(email);
//            if (checkDuplicateEmail.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
//                return checkDuplicateEmail;
//            }
        // 중복이면 EmailDuplicateException 발생 → 글로벌 핸들러가 400 JSON 반환
        userValidateService.checkDuplicateEmail(email);
        // SMTP 실패 시 SmtpSendFailedException → 글로벌 핸들러가 421 JSON 반환
        mailService.signupCertificateMail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 회원정보 찾기 페이지
    @Operation(summary = MgrSwaggerDoc.User.Api.page.FIND_INFO_SUMMARY, description = MgrSwaggerDoc.User.Api.page.FIND_INFO_DESC)
    @GetMapping("/user/find-account")
    public ModelAndView findUserInfo() {
        return new ModelAndView("loginSignUp/findUserInfo");
    }


    // 이메일 찾기
    @Operation(summary = MgrSwaggerDoc.User.Api.findUserAccount.Summary, description = MgrSwaggerDoc.User.Api.findUserAccount.Desc)
    @PostMapping("/user/account")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    public List<String> findUserAccount(@RequestParam("userNickname") String userNickname, @RequestParam("phone") String phone) {
        List<String> userEmail = userInfoService.findUserEmail(userNickname, phone);
        return userEmail;
    }


    @Operation(summary = MgrSwaggerDoc.User.Api.sendReissuePwtCode.Summary, description = MgrSwaggerDoc.User.Api.sendReissuePwtCode.Desc)
    @PostMapping("/user/password/mail-send")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND, ErrorCode.SMTP_SEND_FAILED })
    public ResponseEntity<String> issuePwdCertificateMail(@RequestParam String email) {
        try {
            String chkSendCode = mailService.sendPwdCertificateMail(email).getBody();
            if ("UserNotFound".equals(chkSendCode)){
                return new ResponseEntity<>("UserNotFound", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("FailedUpdatePassword", HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = MgrSwaggerDoc.User.Api.sendReissuePwt.Summary, description = MgrSwaggerDoc.User.Api.sendReissuePwt.Desc)
    @PostMapping("/user/password/reissue")
    @ApiResult(
            errors = { ErrorCode.MAIL_CODE_NOT_EQUAL_400, ErrorCode.MAIL_CODE_EXPIRED_400 })
    public ResponseEntity<String> reissuePassword(@RequestParam String email, @RequestParam String mailCode) {
        try {
            String newPwd = mailService.sendNewPwdMail(email);
            String updatePwd = userInfoService.updateUserPwd(email, newPwd).getBody();
            if (("UserNotFound").equals(updatePwd)){
                return new ResponseEntity<>("UserNotFound", HttpStatus.BAD_REQUEST);
            }

            mailService.checkMailCode(email, mailCode);

//            String checkMailCode = mailService.checkMailCode(email, mailCode).getBody();
//            if ("notEqualMailCode".equals(checkMailCode)){
//                return new ResponseEntity<>("notEqualMailCode", HttpStatus.BAD_REQUEST);
//            }
//            if ("expirationMailCode".equals(checkMailCode)){
//                return new ResponseEntity<>("expirationMailCode", HttpStatus.BAD_REQUEST);
//            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("FailedUpdatePassword", HttpStatus.BAD_REQUEST);
        }
    }

}