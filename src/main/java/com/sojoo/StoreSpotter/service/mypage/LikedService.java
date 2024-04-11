package com.sojoo.StoreSpotter.service.mypage;

import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
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

    @Transactional
    @TimeTrace
    public void storeLiked (User user, String regionName, String industryId, LikedDto likedDto){
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
    @TimeTrace
    public void editLiked(User user, String likedName, String editName) {
        Optional<Liked> likedOptional = getLikedOptional(user, likedName);

        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();
            liked.UpdateLikedName(editName);
        }
    }

    public ResponseEntity<String> duplicateLikedName(User user, String likedName){
        Optional<Liked> likedOptional = getLikedOptional(user, likedName);

        if (likedOptional.isPresent()){
            return new ResponseEntity<>("DuplicateLikedName", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Transactional
    @TimeTrace
    public void removeLiked(User user, String likedName){
        Optional<Liked> likedOptional = getLikedOptional(user, likedName);

        if (likedOptional.isPresent()){
            Liked liked = likedOptional.get();
            likedRepository.delete(liked);
        }
    }

    private Optional<Liked> getLikedOptional(User user, String likedName){
        List<Liked> likedList = likedRepository.findByUser(user);
        return likedList.stream().filter(liked -> likedName.equals(liked.getLikedName()))
                .findFirst();
    }

    @Transactional
    public List<Liked> likedSearch(String keyword) {
        return likedRepository.findByLikedNameContaining(keyword);
    }

    public List<LikedDto> likedEntityToDto(List<Liked> likedList){
        return likedList.stream()
                .map(LikedDto::new)
                .collect(Collectors.toList());
    }

}
