package com.sojoo.StoreSpotter.controller.main;

import com.sojoo.StoreSpotter.dto.storePair.DataRecommend;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    @ResponseBody
        public List<DataRecommend> chooseIndust (
                @RequestParam("indust") String indust,
                @RequestParam("region") String region,
                @RequestParam("dist") String dist) {

        return dataRecommendService.selectPairByDist(indust, region, dist);
        }
    }
