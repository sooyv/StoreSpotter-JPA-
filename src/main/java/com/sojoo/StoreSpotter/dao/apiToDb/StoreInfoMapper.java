package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StoreInfoMapper {
    // pubilcAPIData 데이터베이스 저장 메서드
    void insertApiData(StoreInfo storeInfo, @Param("indust_id") String indust_id);

    // 데이터 업데이트시 기존 데이터 삭제 메서드
    void deleteIndustTable(@Param("indust_id") String indust_id);

}

