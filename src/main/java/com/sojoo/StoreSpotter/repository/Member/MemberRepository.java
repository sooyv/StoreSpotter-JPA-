package com.sojoo.StoreSpotter.repository.Member;
import com.sojoo.StoreSpotter.dto.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 모든 회원 정보 찾기
    List<Member> findAll();

    // email로 회원 찾기
    Optional<Member> findByMemberEmail(String memberEmail);

    // 회원 한명 조회
    Member getOne(Long userId);
}
