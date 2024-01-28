package com.sojoo.StoreSpotter.service.member;

import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 사용자 이메일로 사용자 정보 가져오는 메서드
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("userDetailService loadUserByUsername : " + email);
//        return memberRepository.findByMemberEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException(email + ": 사용자를 찾을 수 없습니다."));
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("userDetailService loadUserByUsername : " + email);
        return memberRepository.findByMemberEmail(email)
                .map(this::creatMemberDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + ": 사용자를 찾을 수 없습니다."));
    }

    // 해당하는 Member의 데이터가 존재한다면 UserDetails 객체로 만들어 리턴
    private UserDetails creatMemberDetails(Member member) {
        return Member.builder()
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPassword(member.getMemberPassword())
                .memberPhone(member.getMemberPhone())
                .roles(member.getRoles())
                .build();
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("userDetailService loadUserByUsername : " + email);
//        Member member = memberRepository.findByMemberEmail(email).orElseThrow(
//                () -> new IllegalArgumentException(email)
//        );
//
//        return new CustomMemberDetails(member);
//    }

}
