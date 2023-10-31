package com.sojoo.StoreSpotter.controller.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public StoreInfoController(StoreInfoService storeInfoService) {
        this.storeInfoService = storeInfoService;
    }


    @GetMapping("/apiDataSave")
    public List<Industry> Industrys() throws Exception {
        System.out.println("StoreInfoController industry 실행");
        return storeInfoService.industrySave();
    }


//    // deleteApiData() test
//    @GetMapping("/deleteTableData")
//    public void delete() throws Exception {
//        storeInfoService.deleteApiData();
//    }

}
