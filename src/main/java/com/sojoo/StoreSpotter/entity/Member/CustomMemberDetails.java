//package com.sojoo.StoreSpotter.entity.Member;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//public class CustomMemberDetails implements UserDetails {
//
//    private final Member member;
//
//    public CustomMemberDetails(Member member) {
//        this.member = member;
//    }
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("user"));
//    }
//
//    // 사용자 비밀번호 반환
//    @Override
//    public String getPassword() {
//        return member.getMemberPassword();
//    }
//
//    // 사용자 id = email 반환
//    @Override
//    public String getUsername() {
//        return member.getMemberEmail();
//    }
//
//    // 계정 만료 여부 반환
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;    // true -> 잠금되지 않음
//    }
//
//    // 계정 잠금 여부 반환
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;     // true -> 잠금되지 않음
//    }
//
//    // 패스워드의 만료 여부 반환
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;    // true -> 만료되지 않음
//    }
//
//    // 계정 사용 가능 여부 반환
//    @Override
//    public boolean isEnabled() {
//        return true;    // true -> 사용 가능
//    }
//}
