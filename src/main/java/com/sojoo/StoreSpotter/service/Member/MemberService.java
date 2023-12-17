package com.sojoo.StoreSpotter.service.Member;

import com.sojoo.StoreSpotter.dto.Member.Member;
import com.sojoo.StoreSpotter.repository.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";


    // 회원가입
//    public Long join(Member member) {
//        validateDuplicateMember(member);
//        memberRepository.save(member);
//        return member.getMemberId();
//    }
    public ResponseEntity<String> join(Member member) {
        if (validateDuplicateMember(member) != null) {
            return new ResponseEntity<>("validateDuplicateMember", HttpStatus.BAD_REQUEST);
        } else {
            memberRepository.save(member);
        }

        return null;
    }



    // 이메일 중복 검증
//    private void validateDuplicateMember(Member user) {
//        Optional<Member> findUserEmail = memberRepository.findByMemberEmail(user.getMemberEmail());
//        if (!findUserEmail.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 회원입니다");
//        }
//    }
    private ResponseEntity<String> validateDuplicateMember(Member user) {
        Optional<Member> findUserEmail = memberRepository.findByMemberEmail(user.getMemberEmail());
        if (!findUserEmail.isEmpty()) {
            return new ResponseEntity<>("validateDuplicateMember", HttpStatus.BAD_REQUEST);
        }
        return null;
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


}
