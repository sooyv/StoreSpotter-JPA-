package com.sojoo.StoreSpotter.controller.main;

import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    private final DataRecommendService dataRecommendService;

    @Autowired
    public MainController(DataPairService dataPairService, DataRecommendService dataRecommendService) {
        this.dataRecommendService = dataRecommendService;
    }

    @GetMapping("/")
    public ModelAndView index() {

        return new ModelAndView("index/index");
    }

    @GetMapping("/search/recommend")
    public void chooseIndust(@RequestParam String indust, @RequestParam String region, @RequestParam String dist){
        dataRecommendService.selectPairByDist(indust,region, dist);
    }
}
