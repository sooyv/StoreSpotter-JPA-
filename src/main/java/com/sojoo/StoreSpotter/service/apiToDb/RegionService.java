package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.repository.apiToDb.RegionRepository;
import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionService {
    private final RegionRepository regionRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public String regionNameToCode(String region_name) {
        Region region = regionRepository.findAllByRegionName(region_name);
        return String.valueOf(region.getRegionId());
    }
}
