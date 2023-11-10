package com.sojoo.StoreSpotter.controller.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api")
//public class StoreInfoController {
//
//    @Autowired
//    private final StoreInfoService storeInfoService;
//
//    @Autowired
//    public StoreInfoController(StoreInfoService storeInfoService) {
//        this.storeInfoService = storeInfoService;
//    }
//
//    @PostMapping("/saveStoreInfo")
//    public void saveStoreInfo() {
//        storeInfoService.saveStore();
//    }
//}


@RestController
public class StoreInfoController {
    private final StoreInfoService storeInfoService;
    private final DataPairService dataPairService;

    @Autowired
    public StoreInfoController(StoreInfoService storeInfoService, DataPairService dataPairService) {
        this.storeInfoService = storeInfoService;
        this.dataPairService = dataPairService;
    }


    @GetMapping("/apiDataSave")
    public List<Industry> Industrys() throws Exception {
        return storeInfoService.industrySave();
    }

    @GetMapping("/DataPair")
    public void DataPairs() throws Exception {
        dataPairService.selectDataPair();
    }


}
