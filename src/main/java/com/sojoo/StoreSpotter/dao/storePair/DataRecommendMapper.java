package com.sojoo.StoreSpotter.dao.storePair;

import com.sojoo.StoreSpotter.dto.storePair.DataRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataRecommendMapper {
    List<DataRecommend> selectByDist(@Param("indust_id") String indust_id, @Param("region_fk") String region_fk, @Param("dist") String dist);

    Double avgDist(@Param("indust_id") String indust_id, String region_fk);

}
