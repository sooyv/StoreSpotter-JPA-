package com.sojoo.StoreSpotter.repository.mypage;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    List<Liked> findByUser(User user);

    List<Liked> findByLikedNameContaining(String keyword);

    Optional<Liked> findByLikedName(String name);

}
