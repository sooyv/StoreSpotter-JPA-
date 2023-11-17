package com.sojoo.StoreSpotter.dao.storePair;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.util.Pair;

import java.util.List;

@Mapper
public interface DataPairMapper {
    //전체 업종 관리하기
    List<StoreInfo> selectIndustryData(@Param("indust_id") String indust_id) throws Exception;

    // 편의점 테이블 데이터 가져오기
     List<StoreInfo> selectConvenienceData() throws Exception;

    // 카페 테이블 데이터 가져오기
    List<StoreInfo> selectCafeData();

    // 편의점 테이블 데이터 삽입
    void insertPairData(PairData pairData, @Param("indust_id") String indust_id);

//    List<ToPairData> distanceSphere(@Param("convenienceData") String point, Integer region);
    List<PairData> distanceSphere(@Param("name") String name, @Param("point") String point, @Param("region") Integer region, @Param("indust_id") String indust_id);


    // 각 pair table에 저장 메서드
    void insertConveniencePairTable();

    void insertCafePairTable();
}
