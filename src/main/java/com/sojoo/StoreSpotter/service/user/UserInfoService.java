package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserInfoService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Autowired
    public UserInfoService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, RedisService redisService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.redisService = redisService;
    }


    // --------------- 비밀번호 재발급 -------------
    @Transactional
    public ResponseEntity<String> updateUserPwd(String email, String newPwd) {
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            user.get().updatePassword(bCryptPasswordEncoder.encode(newPwd));
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("UserNotFound", HttpStatus.BAD_REQUEST);
        }
    }


    // --------------- 이메일 찾기 -------------
    public List<String> findUserEmail(String userNickname, String userPhone) {
        List<User> users = userRepository.findByNicknameAndPhone(userNickname, userPhone);
        List<String> usernameList = users.stream().map(User::getUsername).collect(Collectors.toList());

        return usernameList;
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
    public ResponseEntity<String> modifyPhone(User user, String phone) {
        if (phoneRegExp(phone)) {
            user.updatePhone(phone);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("phoneRegExp", HttpStatus.BAD_REQUEST);
        }
    }

    // --------------- 비밀번호 변경 -------------
    @Transactional
    public ResponseEntity<String> modifyPassword(User user, UserPwdDto userPwdDto) {
        if (checkCurrentPassword(user, userPwdDto.getCurrentPwd())) {
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

    private boolean checkCurrentPassword(User user, String currentPassword) {
        return bCryptPasswordEncoder.matches(currentPassword, user.getPassword());
    }

    private boolean checkEqualPassword(UserPwdDto userPwdDto) {
        return userPwdDto.getChangePwd().equals(userPwdDto.getChangeChkPwd());
    }

    private boolean passwordRegExp(UserPwdDto userPwdDto) {
        String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
        return userPwdDto.getChangePwd().matches(pwRegExp);
    }

    private boolean phoneRegExp(String phone) {
        String phoneRegExp = "\\d{11}";
        return phone.matches(phoneRegExp);
    }


    @Transactional
    public ResponseEntity<String> userWithdraw(User user) {
        String username = user.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            userRepository.delete(user);
            redisService.delValues(username);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("withdrawFailed", HttpStatus.BAD_REQUEST);
        }
    }
}
