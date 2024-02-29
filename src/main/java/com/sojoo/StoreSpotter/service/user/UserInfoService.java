package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class UserInfoService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MailService mailService;

    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    private final String phoneRegExp = "\\d{11}";

    public UserInfoService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, UserService userService, MailService mailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }


    // --------------- 비밀번호 재발급 -------------
    @Transactional
    public String updateUserPw(String email) throws Exception {
        Optional<User> user = userService.findUser(email);
        if (user.isPresent()) {
            String code = mailService.sendPwMail(email);

            user.get().updatePassword(bCryptPasswordEncoder.encode(code));
            return "Successfully reissuePassword";
        } else {
            throw new NoSuchElementException("존재하지 않는 이메일입니다");
        }
    }



    // --------------- 이메일 찾기 -------------
    public List<String> findUserEmail(String username, String userPhone) throws IllegalStateException {
        List<User> users = userRepository.findByNicknameAndPhone(username, userPhone);
        List<String> userEmail = new ArrayList<>();
        for (User user : users) {
            String email = user.getUsername();
            userEmail.add(email);
        }
        if (users.size() <= 0) {
//            throw new IllegalStateException("User not found");
            userEmail = null;
            log.info("User not found");
        }

        return userEmail;
    }

    // --------------- Nickname 변경 -------------
    @Transactional
    public void modifyNickname(User user, String nickname){
        user.updateNickname(nickname);
    }

    // --------------- Phone 변경 -------------
    @Transactional
    public ResponseEntity<String> modifyPhone(User user, String phone){
        if(phoneRegExp(phone)){
            user.updatePhone(phone);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("phoneRegExp", HttpStatus.BAD_REQUEST);
        }
    }

    // --------------- 비밀번호 변경 -------------
    @Transactional
    public ResponseEntity<String> modifyPassword(User user, UserPwdDto userPwdDto){
        if(checkCurrentPassword(user, userPwdDto.getCurrentPwd())){
            user.updatePassword(bCryptPasswordEncoder.encode(userPwdDto.getChangePwd()));
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else if (!checkEqualPassword(userPwdDto)) {
            return new ResponseEntity<>("notEqualPassword", HttpStatus.BAD_REQUEST);
        } else if (!passwordRegExp(userPwdDto)) {
            return new ResponseEntity<>("passwordRegExp", HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>("incorrect password", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkCurrentPassword(User user, String currentPassword){
        return bCryptPasswordEncoder.matches(currentPassword, user.getPassword());
    }

    // 비밀번호 일치 검사
    private boolean checkEqualPassword(UserPwdDto userPwdDto) {
        return userPwdDto.getChangePwd().equals(userPwdDto.getChangeChkPwd());
    }

    // 비밀번호 정규식 검사
    private boolean passwordRegExp(UserPwdDto userPwdDto) {
        return userPwdDto.getChangePwd().matches(pwRegExp);
    }


    // 전화번호 형식 검사
    private boolean phoneRegExp(String phone) {
        return phone.matches(phoneRegExp);
    }



}
