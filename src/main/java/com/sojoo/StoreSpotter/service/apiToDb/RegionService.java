package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionService {
    private final RegionMapper regionMapper;

    @Autowired
    public RegionService(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    public String regionNameToCode(String region_name) {
        return regionMapper.selectRegionCode(region_name);
    }
}
