package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.entity.user.Authority;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.repository.user.AuthorityRepository;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.jwt.exception.NotFoundMemberException;
import com.sojoo.StoreSpotter.jwt.securityUtil.SecurityUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public void signup(UserDto userDto) {
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

        UserDto.from(userRepository.save(user));
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
                String username = tokenProvider.getUsernameFromToken(accessToken);
                Optional<User> users = userRepository.findByUsername(username);
                return users.orElse(null);
            }
        }
        return null;
    }
}