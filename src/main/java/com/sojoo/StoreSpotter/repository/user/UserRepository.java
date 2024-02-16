package com.sojoo.StoreSpotter.repository.user;

import com.sojoo.StoreSpotter.entity.Member.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    Optional<User> findByUsername(String username);

//    List<User> findByUsernameAndPhone(String userName, String userPhone);

    List<User> findByNicknameAndPhone(String userName, String userPhone);
}