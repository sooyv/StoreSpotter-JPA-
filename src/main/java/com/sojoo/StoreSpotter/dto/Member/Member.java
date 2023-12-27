package com.sojoo.StoreSpotter.dto.Member;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long memberId;
    @Column(name = "member_name", nullable = false)
    private String memberName;
    @Column(name = "member_email", unique = true, nullable = false) // 유일한 값만 저장 가능. 중복 저장 불가
    private String memberEmail;
    @Column(name = "member_password", nullable = false)
    private String memberPassword;
    @Column(name = "member_phone", nullable = false)
    private String memberPhone;

    @Builder
    public Member(String memberEmail, String memberPassword, String auth) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
    }

    @Override       // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자 id = email 반환
    @Override
    public String getUsername() {
        return memberEmail;
    }

    // 사용자 비밀번호 반환
    @Override
    public String getPassword() {
        return memberPassword;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;    // true -> 잠금되지 않음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;    // true -> 잠금되지 않음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;    // true -> 만료되지 않음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;    // true -> 사용 가능
    }


}
