package com.sojoo.StoreSpotter.service.member;

import com.sojoo.StoreSpotter.jwt.config.JwtTokenProvider;
import com.sojoo.StoreSpotter.dto.member.MemberDto;
import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

    // 로그인
    public String login(String email, String password) {
        // login email, password 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

//        String token = jwtTokenProvider.generateToken(authentication);

        System.out.println("MemberService login" + email);
        System.out.println("MemberService login" + password);
        return null;
    }

    // 회원가입
    @Transactional
    public Long joinMember(MemberDto memberDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return memberRepository.save(Member.builder()
                .memberName(memberDto.getName())
                .memberEmail(memberDto.getEmail())
                .memberPassword(encoder.encode(memberDto.getPassword()))
                .memberPhone(memberDto.getPhone())
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getMemberId();
    }


    // 이메일 중복 검증
    @PostMapping("/signup/checkemail")
    @ResponseBody
    public String checkId(String email, String type) {
        if (type.equals("email")) {
            Optional<Member> users = memberRepository.findByMemberEmail(email);
            if (users.isEmpty()) {
                return "0";
            }
            return "1";
        }
        return "0";
    }


    // 모든 항목 일치 검사
    public ResponseEntity<String> notNullMemberInfo(MemberDto memberDto) {
        if (memberDto.getName() == null || memberDto.getName() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (memberDto.getEmail() == null || memberDto.getEmail() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (memberDto.getPassword() == null || memberDto.getPassword() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (memberDto.getCheckPassword() == null || memberDto.getCheckPassword() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (memberDto.getPhone() == null || memberDto.getPhone() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 비밀번호 일치 검사
    public ResponseEntity<String> checkEqualPassword(MemberDto memberDto) {
        if (!memberDto.getPassword().equals(memberDto.getCheckPassword())) {
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

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }
}
