package com.sojoo.StoreSpotter.dao.storePair;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataPairMapper {
    // 테이블 데이터 가져오기, indust_id 파라미터 통해 각 테이블 구분
     List<StoreInfo> selectIndustryData(@Param("indust_id") String indust_id) throws Exception;

    void insertPairData(PairData pairData, @Param("indust_id") String indust_id);

    List<PairData> distanceSphere(String st_nm, String coordinates, Integer region, String indust_id);

}
