package com.sojoo.StoreSpotter.controller.main;

import com.sojoo.StoreSpotter.repository.storePair.DataRecommandProjection;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RestController
public class MainController {
    private final DataRecommendService dataRecommendService;
    private final RegionService regionService;
    private final IndustryService industryService;

    @Autowired
    public MainController(DataRecommendService dataRecommendService, RegionService regionService, IndustryService industryService) {
        this.dataRecommendService = dataRecommendService;
        this.regionService = regionService;
        this.industryService = industryService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index/index");
    }


    // 사이드바 전체검색
    @GetMapping("/search/recommend")
    public List<DataRecommandProjection> searchRecommend (@RequestParam("industry") String industry,
                                                       @RequestParam("region") String region,
                                                       @RequestParam("dist") String dist) {

        String industryId = industryService.getIndustryIdFromName(industry);
        String regionName = regionService.getCityFromAddress(region);
        String regionFk = regionService.getRegionIdFromName(regionName);

        return dataRecommendService.selectPairByDist(industryId, regionFk, dist);
    }

    // 사이드바 업종과 주소 선택시 해당 업종 평균거리 값 가져오기
    @GetMapping ("/search/avg-dist")
    public String searchAvgDist(@RequestParam String address, @RequestParam String industry) {

        String industryId = industryService.getIndustryIdFromName(industry);
        String regionName = regionService.getCityFromAddress(address);
        String regionFk = regionService.getRegionIdFromName(regionName);

        return String.valueOf(dataRecommendService.avgDistance(industryId, regionFk));

    }




}
