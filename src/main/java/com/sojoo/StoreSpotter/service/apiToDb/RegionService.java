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

    public String getRegionIdFromName(String regionName) {
        Region region = regionRepository.findAllByRegionName(regionName);
        return String.valueOf(region.getRegionId());
    }

    // 주소 시도만 자르기
    public String getCityFromAddress(String address) {
        int cityIndex = address.indexOf(" ");
        return (cityIndex != -1) ? address.substring(0, cityIndex) : address;
    }
}
