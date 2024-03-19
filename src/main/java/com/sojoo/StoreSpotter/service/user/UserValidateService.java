package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserValidateService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    public UserValidateService(UserRepository userRepository, RedisService redisService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
    }

    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    private final String phoneRegExp = "\\d{11}";

    // 이메일 중복검사
    public ResponseEntity<String> checkDuplicateEmail(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return new ResponseEntity<>("duplicateEmail", HttpStatus.BAD_REQUEST);
        }else {
            return null;
        }
    }

    // 메일 인증 코드 검사
    public String checkMailCode(UserDto userDto) {
        String inputMailCode = userDto.getMailCode();
        String username = userDto.getUsername();

        String storedMailCode = redisService.getValues(username);

        // 입력메일코드와 저장 메일 코드가 같다면 success
        if (inputMailCode.equals(storedMailCode)) {
            return "success";

        // 입력메일코드와 저장메일코드가 같지 않다면
        } else if (storedMailCode != null) {
            return "notEqualMailCode";

        // 저장된 메일 코드가 없으면, 만료된 것으로 간주
        } else{
            return "expirationMailCode";
        }
    }



}
