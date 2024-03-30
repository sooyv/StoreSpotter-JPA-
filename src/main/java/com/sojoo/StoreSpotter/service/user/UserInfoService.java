package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.UserNotFoundException;
import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.mail.MailService;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MailService mailService;
    private final RedisService redisService;

    @Autowired
    public UserInfoService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, MailService mailService, RedisService redisService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.redisService = redisService;
    }


    // --------------- 비밀번호 재발급 -------------
    @Transactional
    public void updateUserPwd(String email, String newPwd) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            user.get().updatePassword(bCryptPasswordEncoder.encode(newPwd));
        } else {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
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

    /**
     MyPage-> info modify
     */

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
        if (checkCurrentPassword(user, userPwdDto.getCurrentPwd())){
            user.updatePassword(bCryptPasswordEncoder.encode(userPwdDto.getChangePwd()));
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        if (!checkEqualPassword(userPwdDto)) {
            return new ResponseEntity<>("notEqualPassword", HttpStatus.BAD_REQUEST);
        }
        if (!passwordRegExp(userPwdDto)) {
            return new ResponseEntity<>("passwordRegExp", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("incorrect password", HttpStatus.BAD_REQUEST);
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
        String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        return userPwdDto.getChangePwd().matches(pwRegExp);
    }

    // 전화번호 형식 검사
    private boolean phoneRegExp(String phone) {
        String phoneRegExp = "\\d{11}";
        return phone.matches(phoneRegExp);
    }


    // 계정 탈퇴
    @Transactional
    public ResponseEntity<String> userWithdraw(User user) {
        // username으로 해당 user 찾기
        String username = user.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()){
            // 해당 유저 삭제
            userRepository.delete(user);
            // redis 삭제
            redisService.delValues(username);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            // user 못참음 exception
            return new ResponseEntity<>("withdrawFailed", HttpStatus.BAD_REQUEST);
        }
    }
}
