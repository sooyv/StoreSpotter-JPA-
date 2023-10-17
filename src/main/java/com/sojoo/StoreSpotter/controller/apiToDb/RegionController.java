package com.sojoo.StoreSpotter.controller.apiToDb;

import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegionController {

    private final StoreInfoService storeInfoService;

    @Autowired
    public RegionController(StoreInfoService storeInfoService) {
        this.storeInfoService = storeInfoService;
    }

}
