package com.sojoo.StoreSpotter.controller.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/saveStoreInfo")
//    public ResponseEntity<String> fetchData() throws Exception {
//        System.out.println("saveStoreInfo 동작");
////        storeInfoService.fetchDataFromPublicAPI();
//
//        return ResponseEntity.ok("Data fetched successfully!");
//    }

//    @GetMapping("/deleteTableData")
//    public void delete() throws Exception {
//        storeInfoService.deleteApiData();
//    }

}
