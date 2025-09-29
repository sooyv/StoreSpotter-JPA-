package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.common.error.ErrorResponse;
import com.sojoo.StoreSpotter.common.exception.EmailDuplicateException;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserValidateService {

    private final UserRepository userRepository;

    @Autowired
    public UserValidateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkDuplicateEmail(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
//            return new ResponseEntity<>("duplicateEmail", HttpStatus.BAD_REQUEST);
            throw new EmailDuplicateException();
        }
    }

}
