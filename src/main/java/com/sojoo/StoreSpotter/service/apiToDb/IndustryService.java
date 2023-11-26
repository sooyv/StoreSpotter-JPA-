package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndustryService {
    private final IndustryMapper industryMapper;

    @Autowired
    public IndustryService(IndustryMapper industryMapper) {
        this.industryMapper = industryMapper;
    }

    public String industryNameToCode(String indust_name) {
        String indust_id = industryMapper.selectIndustryCode(indust_name);
        return indust_id;
    }
}
