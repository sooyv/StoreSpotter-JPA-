package com.sojoo.StoreSpotter.controller.PairData;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
public class PairDataController {
    private final DataPairService dataPairService;
    private final DataRecommendService dataRecommendService;

    @Autowired
    public PairDataController(DataPairService dataPairService, DataRecommendService dataRecommendService) {
        this.dataPairService = dataPairService;
        this.dataRecommendService = dataRecommendService;
    }

    @GetMapping("/DataPair")
    public void DataPairs() throws Exception {
        dataPairService.save_industryPairData();
    }

//    @GetMapping("/DataByDist")
//    public void DataByDist() throws Exception{
//        dataRecommendService.selectPairByDist();
//    }
}
