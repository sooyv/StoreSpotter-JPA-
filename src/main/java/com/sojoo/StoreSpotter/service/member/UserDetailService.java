package com.sojoo.StoreSpotter.service.member;

import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 해당하는 Member의 데이터가 존재한다면 UserDetails 객체로 만들어 리턴
    // 사용자 이메일로 사용자 정보 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("userDetailService loadUserByUsername : " + email);
        return memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + ": 사용자를 찾을 수 없습니다."));
    }

}
