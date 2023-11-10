package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class industryService {
    private final IndustryMapper industryMapper;

    @Autowired
    public industryService(IndustryMapper industryMapper) {
        this.industryMapper = industryMapper;
    }

}
