package com.sojoo.StoreSpotter.controller.mypage;

import com.sojoo.StoreSpotter.api.ApiResult;
import com.sojoo.StoreSpotter.api.MgrSwaggerDoc;
import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.dto.mypage.LikedDto;
import com.sojoo.StoreSpotter.dto.mypage.LikedRequestDto;
import com.sojoo.StoreSpotter.dto.user.UserPwdDto;
import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.service.mypage.LikedService;
import com.sojoo.StoreSpotter.service.user.UserInfoService;
import com.sojoo.StoreSpotter.service.user.UserService;
import com.sojoo.StoreSpotter.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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


    @Operation(summary = MgrSwaggerDoc.Mypage.Api.page.MYPAGE_SUMMARY, description = MgrSwaggerDoc.Mypage.Api.page.MYPAGE_DESC)
    @GetMapping
    public ModelAndView mypage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                               HttpServletRequest request) {

        User user = userService.getUserFromCookie(request);

        List<LikedDto> likedList;

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
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.liked.Summary, description = MgrSwaggerDoc.Mypage.Api.liked.Desc)
    @PostMapping("/liked/add")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND }
    )
    public ResponseEntity<String> addLiked(HttpServletRequest request, @RequestBody LikedRequestDto likedRequest) {

        LikedDto likedDto = likedRequest.getLikedDto();

        String regionName = regionService.getCityFromAddress(likedDto.getAddress());
        String industryId = industryService.getIndustryIdFromName(likedRequest.getIndustryName());

        User user = userService.getUserFromCookie(request);

        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, likedDto.getLikedName());

        if (isDuplicate.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return isDuplicate;

        } else {
            likedService.storeLiked(user, regionName, industryId, likedDto);
            return new ResponseEntity<>("Successfully StoreLiked", HttpStatus.OK);
        }
    }


    // 찜 이름 수정
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.editLiked.Summary, description = MgrSwaggerDoc.Mypage.Api.editLiked.Desc)
    @PostMapping("/liked/edit")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    public ResponseEntity<String> editLiked(HttpServletRequest request,
            @RequestParam String likedName, @RequestParam String editName){

        User user = userService.getUserFromCookie(request);

        ResponseEntity<String> isDuplicate = likedService.duplicateLikedName(user, editName);
        if (isDuplicate.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return isDuplicate;
        }
        likedService.editLiked(user, likedName, editName);
        return new ResponseEntity<>("Successfully EditLiked", HttpStatus.OK);

    }


    // 찜 삭제
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.deleteLiked.Summary, description = MgrSwaggerDoc.Mypage.Api.deleteLiked.Desc)
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    @PostMapping("/liked/remove")
    public void removeLiked(HttpServletRequest request,
                            @RequestParam String likedName) {

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
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.modifyName.Summary, description = MgrSwaggerDoc.Mypage.Api.modifyName.Desc)
    @PostMapping("/info/modify/nickname")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    public void modifyNickname(HttpServletRequest request, @RequestParam String nickname) {
        User user = userService.getUserFromCookie(request);

        userInfoService.modifyNickname(user, nickname);
    }

    // 회원 번호 수정
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.modifyPhone.Summary, description = MgrSwaggerDoc.Mypage.Api.modifyPhone.Desc)
    @PostMapping("/info/modify/phone")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    public ResponseEntity<String> modifyPhone(HttpServletRequest request, @RequestParam String phone) {
        User user = userService.getUserFromCookie(request);

        return userInfoService.modifyPhone(user, phone);
    }


    // 회원 비밀번호 수정
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.modifyPhone.Summary, description = MgrSwaggerDoc.Mypage.Api.modifyPhone.Desc)
    @PostMapping("/info/modify/password")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    public ResponseEntity<String> modifyPwd(HttpServletRequest request, @RequestBody UserPwdDto userPwdDto) {
        User user = userService.getUserFromCookie(request);

        return userInfoService.modifyPassword(user, userPwdDto);
    }

    // 계정 탈퇴
    @Operation(summary = MgrSwaggerDoc.Mypage.Api.modifyPhone.Summary, description = MgrSwaggerDoc.Mypage.Api.modifyPhone.Desc)
    @PostMapping("/info/modify/withdraw")
    @ApiResult(
            errors = { ErrorCode.USER_NOT_FOUND })
    public ResponseEntity<String> userWithdraw(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserFromCookie(request);

        CookieUtil.delCookie(response);
        return userInfoService.userWithdraw(user);
    }

}
