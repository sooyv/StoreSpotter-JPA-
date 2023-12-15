package com.sojoo.StoreSpotter.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class SignUpService {
    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

    // 모든 항목 일치 검사
    public ResponseEntity<String> notNullMemberInfo(String name, String email, String password,
                                                    String checkPassword, String phone) {
        if (name == null || name == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (email == null || email == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (password == null || password == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (checkPassword == null || checkPassword == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (phone == null || phone == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 비밀번호 일치 검사
    public ResponseEntity<String> checkEqualPassword(String password, String checkPassword) {
        if (!password.equals(checkPassword)) {
            return new ResponseEntity<>("notEqualPassword", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 비밀번호 정규식 검사
    public ResponseEntity<String> passwordRegExp(String password) {
        if (!password.matches(pwRegExp)) {
            return new ResponseEntity<>("passwordRegExp", HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
