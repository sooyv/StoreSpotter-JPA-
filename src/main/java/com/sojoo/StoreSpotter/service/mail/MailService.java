package com.sojoo.StoreSpotter.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    // 메일 메시지 작성
    private MimeMessage createMailMessage(String email, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);         // 보내는 대상

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

    // 회원가입 메일 인증
    public String sendMail(String email) throws MessagingException, UnsupportedEncodingException {
        String code = createCode();
        //메일전송에 필요한 정보 설정
        MimeMessage emailMsg = createMailMessage(email, code);

        try {
            // 메일 전송
            javaMailSender.send(emailMsg);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalStateException();
        }

        return code;
    }

    // 랜덤 인증 코드
    private String createCode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {           // 인증코드 8자리
            int index = rnd.nextInt(4); // 0~2 까지 랜덤

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
}
