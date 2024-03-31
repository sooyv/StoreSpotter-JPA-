package com.sojoo.StoreSpotter.service.mail;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.SmtpSendFailedException;
import com.sojoo.StoreSpotter.common.exception.UserNotFoundException;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Autowired
    public MailService(JavaMailSender javaMailSender, RedisService redisService, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.redisService = redisService;
        this.userRepository = userRepository;
    }


    // --------------------- 회원가입 메일 인증코드 ---------------------
    public void signupCertificateMail(String email) {
        try {
            String randomCode = createRandomCode();
            MimeMessage mailMsg = createMailMessage(email, randomCode);
            javaMailSender.send(mailMsg);

            redisService.setValues(email, randomCode, 3, TimeUnit.MINUTES);

        } catch (Exception e) {
            throw new SmtpSendFailedException(ErrorCode.SMTP_SEND_FAILED);
        }
    }

    // --------------------- 비밀번호 재발급 ---------------------
    public ResponseEntity<String> sendPwdCertificateMail(String email) {

        try {
            Optional<User> userOptional = userRepository.findByUsername(email);
            if (userOptional.isPresent()) {
                String mailCode = createRandomCode();
                MimeMessage message = createMailMessage(email, mailCode);
                javaMailSender.send(message);
                redisService.setValues(email, mailCode, 3, TimeUnit.MINUTES);
                return new ResponseEntity<>("success", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("UserNotFound", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("확인용1");
            throw new SmtpSendFailedException(ErrorCode.SMTP_SEND_FAILED);
        }
    }

    public String sendNewPwdMail(String email) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(email);
            if (userOptional.isPresent()) {
                String newPwd = createRandomCode();
                MimeMessage message = createReissuePwdMessage(email, newPwd);
                javaMailSender.send(message);

                return newPwd;
            } else {
                throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new SmtpSendFailedException(ErrorCode.SMTP_SEND_FAILED);
        }
    }

    // 메일 메서지 생성

    private MimeMessage createReissuePwdMessage(String email, String randomCode) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("StoreSpotter 비밀번호 재발급");

        String pwmsg = "";
        pwmsg += "<h1>StoreSpotter 임시비밀번호 발급.</h1>";
        pwmsg += "<div style='font-size:130%'>";
        pwmsg += "임시비밀번호 : <strong>";
        pwmsg += randomCode + "</strong><div><br/>";
        pwmsg += "</div>";
        message.setText(pwmsg, "utf-8", "html");

        message.setFrom(new InternetAddress("techsupp@naver.com"));

        return message;
    }

    private MimeMessage createMailMessage(String email, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("StoreSpotter 인증메일 발송");

        String msg = "";
        msg += "<h1>StoreSpotter 이메일 인증번호입니다.</h1>";
        msg += "<div style='font-size:130%'>";
        msg += "인증 코드 : <strong>";
        msg += code + "</strong><div><br/>";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");

        message.setFrom(new InternetAddress("techsupp@naver.com"));

        return message;
    }

    //-------------------- 랜덤 인증 코드 생성 --------------------
    private String createRandomCode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {           // 인증코드 8자리
            int index = rnd.nextInt(4);  // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
                case 3:
                    key.append((char) ((int) (rnd.nextInt(15)) + 33));
                    // 특수문자
                    break;
            }
        }
        return key.toString();
    }

    // 메일 인증 코드 검사
    public ResponseEntity<String> checkMailCode(String email, String mailCode) {

        String storedMailCode = redisService.getValues(email);

        if (storedMailCode != null && !storedMailCode.equals(mailCode)) {
            return new ResponseEntity<>("notEqualMailCode", HttpStatus.BAD_REQUEST);
        }
        if (storedMailCode == null) {
            return new ResponseEntity<>("expirationMailCode", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}


