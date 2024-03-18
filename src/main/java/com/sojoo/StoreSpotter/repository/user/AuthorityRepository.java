package com.sojoo.StoreSpotter.repository.user;

import com.sojoo.StoreSpotter.entity.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(String authorityName);
}
