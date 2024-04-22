package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.entity.user.Authority;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.RegionRepository;
import com.sojoo.StoreSpotter.repository.mypage.LikedRepository;
import com.sojoo.StoreSpotter.repository.user.AuthorityRepository;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback(value = true)
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserInfoServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private AuthorityRepository authorityRepository;
    @Autowired private LikedRepository likedRepository;
    @Autowired private RegionRepository regionRepository;
    @Autowired private IndustryRepository industryRepository;

    @Autowired private EntityManager entityManager;


    @Test
    public void getUserAndLikedTest() {

        // given

        Optional<Authority> userAuthorityOptional = authorityRepository.findByAuthorityName("ROLE_USER");
        Authority userAuthority = userAuthorityOptional.get();

        Optional<Region> seoulRegionOptional = regionRepository.findById(11);
        Region seoulRegion = seoulRegionOptional.get();

        Industry convIndustry = industryRepository.findByIndustName("편의점");

        List<Liked> likedList = new ArrayList<>();

        User user1 = User.builder()
                .authority(userAuthority)
                .activated(true)
                .nickname("test1")
                .username("test1@naver.com")
                .password("test1")
                .likedList(likedList)
                .build();

        userRepository.save(user1);

        Liked liked1 = Liked.builder()
                .likedName("test1")
                .address("testAddr1")
                .region(seoulRegion)
                .dist(100.0)
                .center("centerTest1")
                .user(user1)
                .industry(convIndustry)
                .build();

        Liked liked2 = Liked.builder()
                .likedName("test2")
                .address("testAddr2")
                .region(seoulRegion)
                .dist(200.0)
                .center("centerTest2")
                .user(user1)
                .industry(convIndustry)
                .build();

        likedRepository.save(liked1);
        likedRepository.save(liked2);


        // when

        entityManager.clear();

        Optional<User> userOptional = userRepository.findByUsername("test1@naver.com");
        User testUser1 = userOptional.get();

        entityManager.clear();

        Optional<Liked> likedOptional = likedRepository.findByLikedName("test1");
        Liked liked = likedOptional.get();
        String likeUsername = liked.getUser().getUsername();


        // then

        assertThat(user1.getUsername()).isEqualTo(likeUsername);
        assertThat(user1.getLikedList().size()).isEqualTo(2);

    }

}