package com.sojoo.StoreSpotter.repository.member;
import com.sojoo.StoreSpotter.entity.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // email로 회원 찾기
    Optional<Member> findByMemberEmail(String memberEmail);

}
