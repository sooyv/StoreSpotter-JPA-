package com.sojoo.StoreSpotter.repository;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.repository.mypage.LikedRepository;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LikedTest {
    @Autowired  private UserRepository userRepository;
    @Autowired private LikedRepository likedRepository;

    @Test
    @Transactional
    public void likedTest(){
        Optional<User> users = userRepository.findById(3L);
        User user = users.get();

        List<Liked> likedList = likedRepository.findByUser(user);
        Optional<Liked> likeds = likedList.stream().filter(liked -> "저장1".equals(liked.getLikedName()))
                .findFirst();

        Liked liked = likeds.get();
        System.out.println("likedAddr = " + liked.getAddress());


//        user.getLikedList().forEach(liked -> System.out.println("이름 : " + liked.getLikedName()));

    }
}
