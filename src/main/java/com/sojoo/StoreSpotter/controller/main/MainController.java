package com.sojoo.StoreSpotter.controller.main;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // 전체 검색 시 Ajax
    @GetMapping("/search/recommend")
    public void chooseIndust (@RequestParam("indust") String indust,
            @RequestParam("region") String region, @RequestParam("dist") String dist) {

        System.out.println("indust : "+ indust);
        String indust_id = industryService.industryNameToCode(indust);
        System.out.println("indust : " + indust_id);

        System.out.println("region : "+ region);
        String region_name = sido(region);
        String region_fk = regionService.regionNameToCode(region_name);
        System.out.println("region_fk : " + region_fk);

        dist = String.valueOf(dataRecommendService.avgDistance(indust_id, region_fk));
        System.out.println("dist : " + dist);

//        dataRecommendService.selectPairByDist(indust, region, dist);
    }

    // 주소선택 시 Ajax
    @PostMapping("/process-address")
    public void selectRegionCode(@RequestBody String address) {
        System.out.println(address);
        String region_name = sido(address);

        // region_id 가져오기
        regionService.regionNameToCode(region_name);
    }

    // 주소 시도만 자르기
    public String sido(String address) {
        String region_name = "";

        if (address.contains("address")) {
            // "address" 문자열이 포함된 주소 선택 시 (Ajax 값이라면)
            int findsido = address.indexOf(" ");
            region_name = (findsido != -1) ? address.substring(12, findsido) : address;
        } else {
            // "address" 문자열이 포함되지 않은 전체 검색 시 값
            int findsido = address.indexOf(" ");
            region_name = (findsido != -1) ? address.substring(0, findsido) : address;
        }

        return region_name;
    }
}
