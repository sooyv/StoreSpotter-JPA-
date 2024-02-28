package com.sojoo.StoreSpotter.repository.myPage;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.myPage.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    List<Liked> findByUser(User user);

    List<Liked> findByLikedNameContaining(String keyword);

}
