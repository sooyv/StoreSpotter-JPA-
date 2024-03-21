package com.sojoo.StoreSpotter.controller.mypage;

import com.sojoo.StoreSpotter.dto.mypage.LikedDto;
import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.service.mypage.LikedService;
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
@RequestMapping("/mypage")
public class MypageController {
    private final LikedService likedService;
    private final IndustryService industryService;
    private final UserService userService;
    private final UserInfoService userInfoService;
    private final RegionService regionService;

    public MypageController(LikedService likedService,
                            IndustryService industryService, UserService userService,
                            UserInfoService userInfoService, RegionService regionService) {
        this.likedService = likedService;
        this.industryService = industryService;
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.regionService = regionService;
    }

    @GetMapping("/")
    public ModelAndView mypage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);

        /* 검색기능 */
        List<LikedDto> likedList;

        // 검색어 유무 확인
        if (keyword == null){
            likedList = likedService.likedEntityToDto(user.getLikedList());
        }else{
            List<Liked> likedSearch = likedService.likedSearch(keyword);
            likedList = likedService.likedEntityToDto(likedSearch);
        }

        model.addAttribute("likedList", likedList);
        model.addAttribute("keyword", keyword);

        Integer likedCount = user.getLikedList().size();
        model.addAttribute("likedCount", likedCount);

        return new ModelAndView("mypage/myStored");
    }

    // 찜 목록 추가(main 페이지)
    @PostMapping("/liked/add")
    public ResponseEntity<String> addLiked(HttpServletRequest request,
                                            @RequestParam String industry, @RequestParam String likedAddress,
                                           @RequestParam Double dist, @RequestParam String likedName,
                                           @RequestParam String center){
        String regionName = regionService.getCityFromAddress(likedAddress);
        String industryId = industryService.getIndustryIdFromName(industry);

        User user = userService.getUserFromCookie(request);

        // 찜 이름 중복 확인 (likeName duplicate valid)
        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, likedName);
        if (isDuplicate != null){
            return isDuplicate;
        } else {
            likedService.storeLiked(user, regionName, industryId, dist, likedAddress, likedName, center);
            return new ResponseEntity<>("Successfully StoreLiked", HttpStatus.OK);
        }
    }

    // 찜 이름 수정
    @PostMapping("/liked/edit")
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
    @PostMapping("/liked/remove")
    public void removeLiked(HttpServletRequest request,
            @RequestParam String likedName){

        User user = userService.getUserFromCookie(request);

        likedService.removeLiked(user, likedName);
    }


    // 계정정보
    @GetMapping("/info")
    public ModelAndView myInfo(Model model, HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);
        String userPhone = user.getPhone();

        model.addAttribute("userPhone", userPhone); // 모델에 username 추가


        return new ModelAndView("mypage/info");
    }

    // 회원 이름 수정
    @PostMapping("/info/modify/nickname")
    public void modifyNickname(HttpServletRequest request, @RequestParam String nickname){
        User user = userService.getUserFromCookie(request);
        System.out.println("nickname" + nickname);

        userInfoService.modifyNickname(user, nickname);
    }

    // 회원 번호 수정
    @PostMapping("/info/modify/phone")
    public ResponseEntity<String> modifyPhone(HttpServletRequest request, @RequestParam String phone){
        User user = userService.getUserFromCookie(request);

        return userInfoService.modifyPhone(user, phone);
    }

    // 회원 비밀번호 수정
    @PostMapping("/info/modify/password")
    public ResponseEntity<String> modifyPwd(HttpServletRequest request, @RequestBody UserPwdDto userPwdDto){
        User user = userService.getUserFromCookie(request);

        return userInfoService.modifyPassword(user, userPwdDto);
    }

    // 계정 탈퇴
    @PostMapping("/info/modify/withdraw")
    public ResponseEntity<String> userWithdraw(HttpServletRequest request) {
        User user = userService.getUserFromCookie(request);

        return userInfoService.userWithdraw(user);
    }

}
