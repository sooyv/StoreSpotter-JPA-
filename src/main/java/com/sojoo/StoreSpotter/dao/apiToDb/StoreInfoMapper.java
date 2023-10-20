package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StoreInfoMapper {
    void insertIndustryData(StoreInfo storeInfo, @Param("indust_id") String indust_id, @Param("region_id") Integer region_id);

    void deleteIndustRegionTable(@Param("indust_id") String indust_id, @Param("region_id") Integer region_id);
}

