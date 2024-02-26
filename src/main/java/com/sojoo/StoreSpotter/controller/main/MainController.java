package com.sojoo.StoreSpotter.controller.main;

//import com.sojoo.StoreSpotter.controller.form.memberForm;
import com.sojoo.StoreSpotter.repository.storePair.DataRecommandProjection;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import com.sojoo.StoreSpotter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;


@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RestController
public class MainController {
    private final DataRecommendService dataRecommendService;
    private final RegionService regionService;
    private final IndustryService industryService;
    private final UserService userService;

    @Autowired
    public MainController(DataRecommendService dataRecommendService, RegionService regionService, IndustryService industryService, UserService userService) {
        this.dataRecommendService = dataRecommendService;
        this.regionService = regionService;
        this.industryService = industryService;
        this.userService = userService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAnyRole('USER')")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication, Principal principal) {
//        System.out.println(authentication.getName());
        return new ModelAndView("/index/index");
    }


    // 전체 검색 시 Ajax
    @GetMapping("/search/recommend")
    public List<DataRecommandProjection> searchRecommend (@RequestParam("indust") String indust,
                                                       @RequestParam("region") String region,
                                                       @RequestParam("dist") String dist) {

        String indust_id = industryService.industryNameToCode(indust);
        String region_name = dataRecommendService.sido(region);
        String region_fk = regionService.regionNameToCode(region_name);

        return dataRecommendService.selectPairByDist(indust_id, region_fk, dist);
    }

    // 주소선택 시 Ajax
    @GetMapping ("/avg-dist")
    public String selectRegionCode (@RequestParam String address, @RequestParam String indust) {

        // region_fk, indust_id 가져오기
        String indust_id = industryService.industryNameToCode(indust);
        String region_name = dataRecommendService.sido(address);

        String region_fk = regionService.regionNameToCode(region_name);

        return String.valueOf(dataRecommendService.avgDistance(indust_id, region_fk));

    }




}
