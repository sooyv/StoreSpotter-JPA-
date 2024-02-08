package com.sojoo.StoreSpotter.jwt.Repository;

import com.sojoo.StoreSpotter.entity.Member.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}