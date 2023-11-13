package com.sojoo.StoreSpotter.dao.storePair;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper
public interface DataPairMapper {
    // 편의점 테이블 데이터 가져오기
     List<StoreInfo> selectConvenienceData() throws Exception;

    // 카페 테이블 데이터 가져오기
    List<StoreInfo> selectCafeData();

    // 편의점 테이블 데이터 삽입
    List<PairData> insertPairData();

    List<String> distanceSphere(@Param("point") String point, Integer region);

    // 각 pair table에 저장 메서드
    void insertConveniencePairTable();

    void insertCafePairTable();
}
