package com.sojoo.StoreSpotter.controller.main;

import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    private final DataRecommendService dataRecommendService;
    @Autowired
    public MainController(DataRecommendService dataRecommendService) {
        this.dataRecommendService = dataRecommendService;
    }


    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index/index");
    }

    @GetMapping("/search/recommend")
    public void chooseIndust (@RequestParam("indust") String indust,
            @RequestParam("region") String region, @RequestParam("dist") String dist) {
        // indust_name으로 indust_id select
        System.out.println(indust);
        // region_name으로 region_id select
        System.out.println(region);
        // 해당 업종, 지역의 평균거리?? select - 평균거리 하는것 맞는지
        System.out.println(dist);
    dataRecommendService.selectPairByDist(indust, region, dist);

    }

    @PostMapping("/process-address")
    public void processAddress(@RequestBody String address) {
        // 주소선택 ajax로 먼저 받아오기
        System.out.println(address);
        // 뒤에 자를 문자열
        int findsido = address.indexOf(" ");

        String sido = (findsido != -1) ? address.substring(12, findsido) : address;
        System.out.println(sido);

    }

}
