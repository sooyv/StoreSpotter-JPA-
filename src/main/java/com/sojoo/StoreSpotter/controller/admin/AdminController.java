package com.sojoo.StoreSpotter.controller.admin;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import com.sojoo.StoreSpotter.service.storePair.DataRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class AdminController {
    private final DataPairService dataPairService;
    private final StoreInfoService storeInfoService;

    @Autowired
    public AdminController(DataPairService dataPairService, StoreInfoService storeInfoService) {
        this.dataPairService = dataPairService;
        this.storeInfoService = storeInfoService;
    }

    @GetMapping("/admin")
    public ModelAndView index() {
        return new ModelAndView("admin/admin");
    }

    @PostMapping("/DataPair")
    public void DataPairs() throws Exception {
        dataPairService.save_industryPairData();
    }

    @PostMapping("/apiDataSave")
    public List<Industry> Industrys() throws Exception {
        return storeInfoService.industrySave();
    }


}
