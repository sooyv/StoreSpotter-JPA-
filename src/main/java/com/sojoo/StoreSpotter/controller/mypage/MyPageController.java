package com.sojoo.StoreSpotter.controller.mypage;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.myPage.Liked;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.myPage.LikedService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/mypage")
public class MyPageController {

    private final DataRecommendService dataRecommendService;
    private final LikedService likedService;
    private final IndustryService industryService;
    private final UserRepository userRepository;

    public MyPageController(DataRecommendService dataRecommendService, LikedService likedService, IndustryService industryService, UserRepository userRepository) {
        this.dataRecommendService = dataRecommendService;
        this.likedService = likedService;
        this.industryService = industryService;
        this.userRepository = userRepository;
    }


    @GetMapping("/mypage")
    @Transactional
    public ModelAndView myPage(Model model) {
        ////// 유저 예시 -> 토큰에서 user 받아오면 변경 필요
        Optional<User> users = userRepository.findById(3L);
        User user = users.get();
        ////// user 예시 끝

        // model에 likedList 담기
        List<Liked> likedList = user.getLikedList(); // 사용자의 likedList 가져오기
        model.addAttribute("likedList", likedList); // 모델에 likedList 추가

        // model에 likedList 개수 담기
        model.addAttribute("likedCount", likedList.size());


        return new ModelAndView("/myPage/myStored");
    }

    // 찜 목록 추가(main 페이지)
    @PostMapping("/mypage/liked/add")
    public ResponseEntity<String> addLiked(@RequestParam String indust, @RequestParam String likedAddress,
                                           @RequestParam Double dist, @RequestParam String likedName){
        String regionName = dataRecommendService.sido(likedAddress);
        String industId = industryService.industryNameToCode(indust);

        ////// 유저 예시 -> 토큰에서 user 받아오면 변경 필요
        Optional<User> users = userRepository.findById(3L);
        User user = users.get();
        ////// user 예시 끝

        // 찜 이름 중복 확인 (likeName duplicate valid)
        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, likedName);
        if (isDuplicate != null){
            return isDuplicate;
        } else{
            likedService.storeLiked(user, regionName, industId, dist, likedAddress, likedName);
            return new ResponseEntity<>("Successfully StoreLiked", HttpStatus.OK);
        }

    }

    // 찜 이름 수정
    @Transactional
    @PostMapping("/mypage/liked/edit")
    public ResponseEntity<String> editLiked(@RequestParam String likedName, @RequestParam String editName){

        ////// 유저 예시 -> 토큰에서 user 받아오면 변경 필요
        Optional<User> users = userRepository.findById(3L);
        User user = users.get();
        ////// user 예시 끝

        // 찜 이름 중복 확인 (likeName duplicate valid)
        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, editName);
        if (isDuplicate != null){
            return isDuplicate;
        }
        likedService.editLiked(user, likedName, editName);
        return new ResponseEntity<>("Successfully EditLiked", HttpStatus.OK);

    }

    // 찜 삭제
    @PostMapping("/mypage/liked/remove")
    @Transactional
    public void removeLiked(@RequestParam String likedName){

        ////// 유저 예시 -> 토큰에서 user 받아오면 변경 필요
        Optional<User> users = userRepository.findById(3L);
        User user = users.get();
        ////// user 예시 끝

        likedService.removeLiked(user, likedName);
    }

    // 찜 바로가기(지도검색)
//    @GetMapping("mypage/liked/redirect")
//    public void redirectLiked(@RequestParam String likedName){
//
//        ////// 유저 예시 -> 토큰에서 user 받아오면 변경 필요
//        Optional<User> users = userRepository.findById(3L);
//        User user = users.get();
//        ////// user 예시 끝
//
//
//    }
}
