package com.sojoo.StoreSpotter.controller.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndustryController {

    private final StoreInfoService storeInfoService;

    @Autowired
    public IndustryController(StoreInfoService storeInfoService) {
        this.storeInfoService = storeInfoService;
    }

    @GetMapping("/apiDataSave")
    public List<Industry> Industrys() throws Exception {
        System.out.println("industry 컨트롤러 실행");
        return storeInfoService.industrySave();
    }
}
