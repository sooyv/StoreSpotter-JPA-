package com.sojoo.StoreSpotter.service.myPage;

import com.sojoo.StoreSpotter.controller.main.MainController;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import com.sojoo.StoreSpotter.entity.myPage.Liked;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.RegionRepository;
import com.sojoo.StoreSpotter.repository.myPage.LikedRepository;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class LikedService {
    private LikedRepository likedRepository;
    private RegionRepository regionRepository;
    private IndustryRepository industryRepository;
    private UserRepository userRepository;
    private MainController mainController;

    @Autowired
    public LikedService (LikedRepository likedRepository, RegionRepository regionRepository, IndustryRepository industryRepository, UserRepository userRepository, MainController mainController){
        this.likedRepository = likedRepository;
        this.regionRepository = regionRepository;
        this.industryRepository = industryRepository;
        this.userRepository = userRepository;
        this.mainController = mainController;
    }

    /**
     토큰에서 user를 못받아오면 로그인이 필요하다는 메세지와 함께 login 페이지로 리다이렉션 하는 로직 추가 필요
    */

    // 찜목록 저장
    public void storeLiked (User user, String regionName, String industId, Double dist, String address, String likedName){

        Region region = regionRepository.findByRegionName(regionName);
        Industry industry = industryRepository.findOneByIndustId(industId);

        Liked liked = Liked.builder()
                .user(user)
                .region(region)
                .industry(industry)
                .dist(dist)
                .address(address)
                .likedName(likedName)
                .build();

        likedRepository.save(liked);

    }

    // 찜목록 수정
    // private으로 수정 검토
    @Transactional
    public void editLiked(User user, String likedName, String editName) {
        // user와 likedName으로 liked 객체 찾기
        Optional<Liked> likedOptional = likedOptional(user, likedName);

        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();
            // 이름 수정
            liked.EditLikedName(editName);
        }else {
            // Exception 생성 필요
            System.out.println("Liked 객체가 존재하지 않습니다.");
        }
    }

    // 찜목록 likedName 중복 검증
    public ResponseEntity<String> duplicateLikedName(User user, String likedName){
        // user와 likedName으로 liked 객체 찾기
        Optional<Liked> likedOptional = likedOptional(user, likedName);

        if (likedOptional.isPresent()){
            return new ResponseEntity<>("DuplicateLikedName", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Transactional
    //private 확인 필요
    // 찜 목록에서 삭제
    public void removeLiked(User user, String likedName){
        Optional<Liked> likedOptional = likedOptional(user, likedName);

        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();
            likedRepository.delete(liked);
        }
    }

    public void likedRedirect(User user, String likedName){
        Optional<Liked> likedOptional = likedOptional(user, likedName);
        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();

        }

    }


    // user와 likedName으로 liked 객체 찾기
    public Optional<Liked> likedOptional(User user, String likedName){
        List<Liked> likedList = likedRepository.findByUser(user);
        return likedList.stream().filter(liked -> likedName.equals(liked.getLikedName()))
                .findFirst();
    };

}
