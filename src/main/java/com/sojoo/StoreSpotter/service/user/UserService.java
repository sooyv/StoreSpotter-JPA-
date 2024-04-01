package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.EmailDuplicateException;
import com.sojoo.StoreSpotter.common.exception.UserNotFoundException;
import com.sojoo.StoreSpotter.entity.user.Authority;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.repository.user.AuthorityRepository;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import com.sojoo.StoreSpotter.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthorityRepository authorityRepository;
    private final UserValidateService userValidateService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider, AuthorityRepository authorityRepository, UserValidateService userValidateService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
        this.authorityRepository = authorityRepository;
        this.userValidateService = userValidateService;
    }

    public User getUserFromUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public void signup(UserDto userDto) {
        if (userValidateService.checkDuplicateEmail(userDto.getUsername()).getStatusCode().equals(HttpStatus.BAD_REQUEST)){
            throw new EmailDuplicateException(ErrorCode.EMAIL_DUPLICATION);
        }
        Optional<Authority> authorityOptional = authorityRepository.findByAuthorityName("ROLE_USER");
        Authority authority;
        authority = authorityOptional.orElseGet(() -> Authority.builder()
                .authorityName("ROLE_USER")
                .build());

        User user = User.builder()
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .phone(userDto.getPhone())
                .authority(authority)
                .activated(true)
                .build();

        userRepository.save(user);
    }


    public User getUserFromCookie(HttpServletRequest request) {
        String name = "access_token";
//        Cookie[] cookies = request.getCookies();
        Cookie cookie = CookieUtil.getCookie(request, name);
        String accessToken = cookie.getValue();

//        if (cookie == null || cookie.length == 0) {
        if (cookie == null) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

//        Optional<Cookie> tokens = Arrays.stream(cookies)
//                .filter(cookie -> name.equals(cookie.getName()))
//                .findFirst();

//        if (token.isEmpty()){
        if (accessToken.isEmpty()){
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

//        Cookie token = tokens.get();
//        String accessToken = String.valueOf(token.getValue());
        String username = tokenProvider.getUsernameFromToken(accessToken);

        if (username == null) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        /*String refreshToken = redisService.getValues(username);*/
        if (tokenProvider.getRefreshTokenFromAccessToken(accessToken) == null) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return getUserFromUsername(username);
    }


}