package com.sojoo.StoreSpotter.service.mypage;

import com.sojoo.StoreSpotter.dto.mypage.LikedDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.RegionRepository;
import com.sojoo.StoreSpotter.repository.mypage.LikedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikedService {
    private final LikedRepository likedRepository;
    private final RegionRepository regionRepository;
    private final IndustryRepository industryRepository;

    @Autowired
    public LikedService (LikedRepository likedRepository, RegionRepository regionRepository, IndustryRepository industryRepository){
        this.likedRepository = likedRepository;
        this.regionRepository = regionRepository;
        this.industryRepository = industryRepository;
    }


    // 찜목록 저장
    @Transactional
    public void storeLiked (User user, String regionName, String industryId, LikedDto likedDto) {
        Region region = regionRepository.findByRegionName(regionName);
        Optional<Industry> industryOptional = industryRepository.findById(industryId);
        if (industryOptional.isPresent()) {
            Liked liked = Liked.builder()
                    .user(user)
                    .region(region)
                    .industry(industryOptional.get())
                    .dist(likedDto.getDist())
                    .address(likedDto.getAddress())
                    .likedName(likedDto.getLikedName())
                    .center(likedDto.getCenter())
                    .build();

            likedRepository.save(liked);
        }
    }


    @Transactional
    public void editLiked(User user, String likedName, String editName) {
        Optional<Liked> likedOptional = getlikedOptional(user, likedName);

        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();
            liked.UpdateLikedName(editName);
        }
    }

    public ResponseEntity<String> duplicateLikedName(User user, String likedName){
        Optional<Liked> likedOptional = getlikedOptional(user, likedName);

        if (likedOptional.isPresent()){
            return new ResponseEntity<>("DuplicateLikedName", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Transactional
    //private 확인 필요
    // 찜 목록에서 삭제
    public void removeLiked(User user, String likedName){
        Optional<Liked> likedOptional = getlikedOptional(user, likedName);

        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();
            likedRepository.delete(liked);
        }
    }


    // user와 likedName으로 liked 객체 찾기
    public Optional<Liked> getlikedOptional(User user, String likedName){
        List<Liked> likedList = likedRepository.findByUser(user);
        return likedList.stream().filter(liked -> likedName.equals(liked.getLikedName()))
                .findFirst();
    }

    /* search */
    @Transactional
    public List<Liked> likedSearch(String keyword) {
        return likedRepository.findByLikedNameContaining(keyword);
    }

    // List<Liked> -> List<LikedDto>
    public List<LikedDto> likedEntityToDto(List<Liked> likedList){
        return likedList.stream()
                .map(LikedDto::new)
                .collect(Collectors.toList());
    }

}
