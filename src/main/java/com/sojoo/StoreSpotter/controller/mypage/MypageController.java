package com.sojoo.StoreSpotter.controller.mypage;

import com.sojoo.StoreSpotter.dto.mypage.LikedDto;
import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.mypage.LikedService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import com.sojoo.StoreSpotter.service.user.UserInfoService;
import com.sojoo.StoreSpotter.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MypageController {
    private final DataRecommendService dataRecommendService;
    private final LikedService likedService;
    private final IndustryService industryService;
    private final UserService userService;

    private final UserInfoService userInfoService;

    public MypageController(DataRecommendService dataRecommendService, LikedService likedService,
                            IndustryService industryService, UserService userService,
                            UserInfoService userInfoService) {
        this.dataRecommendService = dataRecommendService;
        this.likedService = likedService;
        this.industryService = industryService;
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    /**
     * 찜버튼 기능
     */
    @GetMapping("/mypage")
    public ModelAndView myPage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);

        /* 검색기능 */
        List<LikedDto> likedList = null; // 사용자의 likedList 가져오기
        // model에 likedList 담기
        // 검색어 유무 확인
        if (keyword == null){
            likedList = likedService.likedEntityToDto(user.getLikedList()); // 사용자의 likedList 가져오기
        }else{
            List<Liked> likedSearch = likedService.likedSearch(keyword);
            likedList = likedService.likedEntityToDto(likedSearch);
        }

        model.addAttribute("likedList", likedList); // 모델에 likedList 추가
        model.addAttribute("keyword", keyword); // 모델에 keyword 추가

        // model에 likedList 개수 담기
        Integer likedCount = user.getLikedList().size();
        model.addAttribute("likedCount", likedCount);

        System.out.println(likedList);

        return new ModelAndView("mypage/myStored");
    }

    // 찜 목록 추가(main 페이지)
    @PostMapping("/mypage/liked/add")
    public ResponseEntity<String> addLiked(HttpServletRequest request,
                                            @RequestParam String indust, @RequestParam String likedAddress,
                                           @RequestParam Double dist, @RequestParam String likedName,
                                           @RequestParam String center){
        String regionName = dataRecommendService.sido(likedAddress);
        String industId = industryService.industryNameToCode(indust);

        User user = userService.getUserFromCookie(request);

        // 찜 이름 중복 확인 (likeName duplicate valid)
        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, likedName);
        if (isDuplicate != null){
            return isDuplicate;
        } else{
            likedService.storeLiked(user, regionName, industId, dist, likedAddress, likedName, center);
            return new ResponseEntity<>("Successfully StoreLiked", HttpStatus.OK);
        }


    }

    // 찜 이름 수정
    @Transactional
    @PostMapping("/mypage/liked/edit")
    public ResponseEntity<String> editLiked(HttpServletRequest request,
            @RequestParam String likedName, @RequestParam String editName){

        User user = userService.getUserFromCookie(request);


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
    public void removeLiked(HttpServletRequest request,
            @RequestParam String likedName){

        User user = userService.getUserFromCookie(request);


        likedService.removeLiked(user, likedName);
    }

    /**
     * 찜버튼 기능 끝
     */

    /**
     * 내 계정 정보
     */

    @GetMapping("/mypage/info")
    public ModelAndView myInfo(Model model, HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);
        String userPhone = user.getPhone();

        model.addAttribute("userPhone", userPhone); // 모델에 username 추가


        return new ModelAndView("mypage/info");
    }

    @PostMapping("/mypage/info/modify/nickname")
    public void modifyNickname(HttpServletRequest request, @RequestParam String nickname){
        User user = userService.getUserFromCookie(request);
        System.out.println("nickname" + nickname);

        userInfoService.modifyNickname(user, nickname);
    }

    @PostMapping("/mypage/info/modify/phone")
    @Transactional
    public ResponseEntity<String> modifyPhone(HttpServletRequest request, @RequestParam String phone){
        User user = userService.getUserFromCookie(request);

        return userInfoService.modifyPhone(user, phone);
    }

    @PostMapping("/mypage/info/modify/password")
    @Transactional
    public ResponseEntity<String> modifyPwd(HttpServletRequest request, @RequestBody UserPwdDto userPwdDto){
        User user = userService.getUserFromCookie(request);

        return userInfoService.modifyPassword(user, userPwdDto);
    }



    @PostMapping("/user/withdraw")
    public ResponseEntity<String> withdrawFailed(HttpServletRequest request) {
        User user = userService.getUserFromCookie(request);

        return userInfoService.withdrawFailed(user);
    }

}
