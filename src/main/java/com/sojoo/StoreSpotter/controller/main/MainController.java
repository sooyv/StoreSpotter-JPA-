package com.sojoo.StoreSpotter.controller.main;

import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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
        System.out.println(indust);
        System.out.println(region);
        System.out.println(dist);
    dataRecommendService.selectPairByDist(indust, region, dist);

    }

    @PostMapping("/process-address")
    public void processAddress(@RequestBody String address) {
        System.out.println(address);
        // 여기서 addresses 값을 활용하여 원하는 작업 수행
    }

    }
