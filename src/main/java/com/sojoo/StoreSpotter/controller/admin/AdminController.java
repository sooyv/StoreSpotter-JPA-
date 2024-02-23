package com.sojoo.StoreSpotter.controller.admin;

import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
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
    public List<Industry> Industries() throws Exception {
        return storeInfoService.industrySave();
    }


}
