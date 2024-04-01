package com.sojoo.StoreSpotter.controller.mypage;

import com.sojoo.StoreSpotter.dto.mypage.LikedDto;
import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.service.mypage.LikedService;
import com.sojoo.StoreSpotter.service.user.UserInfoService;
import com.sojoo.StoreSpotter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mypage")
public class MypageController {
    private final LikedService likedService;
    private final IndustryService industryService;
    private final RegionService regionService;
    private final UserService userService;
    private final UserInfoService userInfoService;

    @Autowired
    public MypageController(LikedService likedService,
                            IndustryService industryService, RegionService regionService, UserService userService,
                            UserInfoService userInfoService) {
        this.likedService = likedService;
        this.industryService = industryService;
        this.regionService = regionService;
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    @GetMapping
    public ModelAndView mypage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);

        /* 검색기능 */
        List<LikedDto> likedList;

        // 검색어 유무 확인
        if (keyword == null) {
            likedList = likedService.likedEntityToDto(user.getLikedList()); // 사용자의 likedList 가져오기
        } else {
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
    public ResponseEntity<String> addLiked(HttpServletRequest request, @RequestBody Map<String, Object> likedRequest) {
        System.out.println("여기 타긴 타는건가 1");

        // likedDto 부분 추출
        Map<String, Object> likedDtoMap = (Map<String, Object>) likedRequest.get("likedDto");
        String likedName = (String) likedDtoMap.get("likedName");
        Double dist = ((Number) likedDtoMap.get("dist")).doubleValue();
        String address = (String) likedDtoMap.get("address");
        String center = (String) likedDtoMap.get("center");

        // industry 부분 추출
         String industry = (String) likedRequest.get("industry");

        String regionName = regionService.getCityFromAddress(address);
        String industryId = industryService.getIndustryIdFromName(industry);

        User user = userService.getUserFromCookie(request);

        // 찜 이름 중복 확인 (likeName duplicate valid)
        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, likedName);
        if (isDuplicate != null) {
            return isDuplicate;
        } else {
            likedService.storeLiked(user, regionName, industryId, dist, address, likedName, center);
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


    // 계정 정보
    @GetMapping("/info")
    public ModelAndView myInfo(Model model, HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);
        String userPhone = user.getPhone();

        model.addAttribute("userPhone", userPhone);

        return new ModelAndView("mypage/info");
    }

    // 회원 이름 수정
    @PostMapping("/info/modify/nickname")
    public void modifyNickname(HttpServletRequest request, @RequestParam String nickname){
        User user = userService.getUserFromCookie(request);

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
