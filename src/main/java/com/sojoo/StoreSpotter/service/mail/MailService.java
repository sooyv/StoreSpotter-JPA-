package com.sojoo.StoreSpotter.service.mail;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.user.UserService;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    // --------------------- 메일 인증코드 ---------------------
    // 메일 메시지 작성
    private MimeMessage createMailMessage(String email, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);         // 보내는 대상
        System.out.println("createMailMessage email 대상 확인 : " + email);

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

    // 회원가입 인증 코드 메일
    public void sendMail(String email, String code) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage mailMsg = createMailMessage(email, code);
            javaMailSender.send(mailMsg);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalStateException();
        }
    }

    // 회원가입 인증 코드 메일 전송, 인증 코드 redis 저장
    public String sendCertificationMail(String email) {
        try {
            // 랜덤 인증 코드 생성
            String code = createCode();
            // email, code 순서로
            sendMail(email, code);

            // redis에 인증 코드 저장
//            redisUtil.setDataExpire(code, email, 60*5L); // {key,value} 5분동안 저장.
            return code;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("sendCertificationMail - 메일 전송에 실패했습니다.");
        }
    }

    // --------------------- 비밀번호 재발급 ---------------------
    // 비밀번호 재발급 메일 작성
    private MimeMessage createPwMessage(String email, String code) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);         // 보내는 대상
        System.out.println("createPwMessage email : " + email);
        System.out.println("createPwMessage code : " + code);

        message.setSubject("TECHSUPP 비밀번호 재발급");                     // 제목

        // 비밀번호 재발급
        String pwmsg = "";
        pwmsg += "<h1>StoreSpotter 임시비밀번호 발급.</h1>";
        pwmsg += "<div style='font-size:130%'>";
        pwmsg += "임시비밀번호 : <strong>";
        pwmsg += code + "</strong><div><br/>";
        pwmsg += "</div>";
        message.setText(pwmsg, "utf-8", "html");

        message.setFrom(new InternetAddress("techsupp@naver.com"));

        return message;
    }

    // 비밀번호 재발급 메일 발송
    public String sendPwMail(String email) throws Exception {
        String code = createCode();
        MimeMessage message = createPwMessage(email, code);
        System.out.println("sendPwMail email : " +email);
        System.out.println("sendPwMail code : " +code);

        try {
            javaMailSender.send(message);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalStateException();
        }
        return code;
    }


    //-------------------- 랜덤 인증 코드 생성 --------------------
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
