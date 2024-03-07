package com.sojoo.StoreSpotter.db;

import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import com.sojoo.StoreSpotter.entity.myPage.Liked;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.RegionRepository;
import com.sojoo.StoreSpotter.repository.myPage.LikedRepository;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class dbRollbackTest {

    @Autowired private IndustryRepository industryRepository;
    @Autowired private RegionRepository regionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LikedRepository likedRepository;



    @Test
    @Transactional
    @Rollback(false)
    public void rollbackTest() throws Exception{

        Optional<User> userOptional = userRepository.findOneWithAuthoritiesByUsername("jo");
        User user = userOptional.get();
        Optional<Region> regionOptional = regionRepository.findById(11);
        Optional<Industry> industryOptional = industryRepository.findById("G20405");
        Region region = regionOptional.get();
        Industry industry = industryOptional.get();
        Liked liked1 = new Liked("test1", 1.0, "testAddr1", "TestCenter1", industry, region, user);
        Liked liked2 = new Liked("test2", 1.0, "testAddr2", "TestCenter1", industry, region, user);
        Liked liked3 = new Liked("test3", 1.0, "testAddr3", "TestCenter1", industry, region, user);

        List<Liked> likedList = new ArrayList<>();
        likedList.add(liked1);
        likedList.add(liked2);
        likedList.add(liked3);


        for (int i = 0; i < 3; i++) {
            Liked liked = likedList.get(i);
            likedRepository.save(liked);
        }
        
        List<Liked>likedList1 = likedRepository.findAll();
        for(Liked liked : likedList1){
            System.out.println("liked.getAddress() = " + liked.getAddress());
        }

    }
}
