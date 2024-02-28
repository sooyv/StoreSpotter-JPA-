package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
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

}
