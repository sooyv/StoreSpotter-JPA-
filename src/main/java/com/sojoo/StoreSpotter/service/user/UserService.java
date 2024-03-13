package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.entity.user.Authority;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.jwt.exception.NotFoundMemberException;
import com.sojoo.StoreSpotter.jwt.securityUtil.SecurityUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .phone(userDto.getPhone())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }


    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    public Optional<User> findUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }

    public User getUserFromCookie(HttpServletRequest request) {
        String name = "access_token";
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Optional<Cookie> tokens = Arrays.stream(cookies)
                    .filter(cookie -> name.equals(cookie.getName()))
                    .findFirst();
            if (tokens.isPresent()) {
                Cookie token = tokens.get();
                String accessToken = String.valueOf(token.getValue());
                boolean isToken = tokenProvider.validateToken(accessToken);

                if (isToken){
                    String username = tokenProvider.getUsernameFromToken(accessToken);
                    Optional<User> users = userRepository.findByUsername(username);
                    return users.orElse(null);
                }
            }
        }
        return null;
    }


}