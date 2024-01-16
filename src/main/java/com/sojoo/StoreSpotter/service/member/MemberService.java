package com.sojoo.StoreSpotter.service.member;

import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";


    // 회원가입
    @Transactional
    public Long joinMember(Member memberInfo) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return memberRepository.save(Member.builder()
                .memberName(memberInfo.getMemberName())
                .memberEmail(memberInfo.getMemberEmail())
                .memberPassword(encoder.encode(memberInfo.getPassword()))
                .memberPhone(memberInfo.getMemberPhone())
                .build()).getMemberId();
    }
//    회원가입
//    public ResponseEntity<String> joinMember(Member member) {
//        if (validateDuplicateMember(member) != null) {
//            return new ResponseEntity<>("validateDuplicateMember", HttpStatus.BAD_REQUEST);
//        } else {
//            // 비밀번호 암호화
//            member.setMemberPassword(bCryptPasswordEncoder.encode(member.getPassword()));
//            memberRepository.save(member);
//
//        }
//
//        return null;
//    }


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
    public ResponseEntity<String> notNullMemberInfo(String name, String email, String password,
                                                    String checkPassword, String phone) {
        if (name == null || name == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (email == null || email == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (password == null || password == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (checkPassword == null || checkPassword == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        } else if (phone == null || phone == "") {
            return new ResponseEntity<>("memberInfo", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 비밀번호 일치 검사
    public ResponseEntity<String> checkEqualPassword(String password, String checkPassword) {
        if (!password.equals(checkPassword)) {
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