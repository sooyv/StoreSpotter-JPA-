package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findAll();

    Region findAllByRegionName(String region_name);
}
