package com.sojoo.StoreSpotter.controller.admin;

import com.sojoo.StoreSpotter.common.exception.ApiDataNotFoundException;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final DataPairService dataPairService;
    private final StoreInfoService storeInfoService;

    @Autowired
    public AdminController(DataPairService dataPairService, StoreInfoService storeInfoService) {
        this.dataPairService = dataPairService;
        this.storeInfoService = storeInfoService;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/admin");
    }

    @PostMapping("/dataPair")
    public void dataPairs() {
        dataPairService.saveIndustryPairData();
    }

    @PostMapping("/conv-dataPair")
    public void convDataPairs() {
        dataPairService.saveConvIndustryPairData();
    }

    @PostMapping("/cafe-dataPair")
    public void cafeDataPairs() {
        dataPairService.saveCafeIndustryPairData();
    }

    @PostMapping("/apiDataSave")
    public ResponseEntity<String> industries(){
        try{
            storeInfoService.apiToDb();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(ApiDataNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/conv-api")
    public ResponseEntity<String> convApiSave(){
        try{
            storeInfoService.convApiToDb();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(ApiDataNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cafe-api")
    public ResponseEntity<String> cafeApiSave(){
        try{
            storeInfoService.cafeApiToDb();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(ApiDataNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
