package com.sojoo.StoreSpotter.controller.main;

//import com.sojoo.StoreSpotter.controller.form.memberForm;
import com.sojoo.StoreSpotter.service.apiToDb.IndustryService;
import com.sojoo.StoreSpotter.service.apiToDb.RegionService;
import com.sojoo.StoreSpotter.dto.storePair.DataRecommend;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


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
    public List<DataRecommend> chooseIndust (@RequestParam("indust") String indust,
                                             @RequestParam("region") String region,
                                             @RequestParam("dist") String dist) {

        String indust_id = industryService.industryNameToCode(indust);
        String region_name = sido(region);
        String region_fk = regionService.regionNameToCode(region_name);

        return dataRecommendService.selectPairByDist(indust_id, region_fk, dist);
    }

    // 주소선택 시 Ajax
    @GetMapping ("/avg-dist")
    public String selectRegionCode (@RequestParam String address, @RequestParam String indust) {

        // region_fk, indust_id 가져오기
        String indust_id = industryService.industryNameToCode(indust);
        String region_name = sido(address);

        String region_fk = regionService.regionNameToCode(region_name);

        return String.valueOf(dataRecommendService.avgDistance(indust_id, region_fk));

    }

    // 주소 시도만 자르기
    public String sido (String address) {
            int findsido = address.indexOf(" ");
            String region_name = (findsido != -1) ? address.substring(0, findsido) : address;

        return region_name;
    }


}
