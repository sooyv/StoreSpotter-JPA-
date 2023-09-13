package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfoDTO;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository()
public interface StoreInfoMapper {
    @Insert("INSERT INTO store_info (bizesId, bizesNm, rdnmAdr) VALUES (#{bizesId}, #{bizesNm}, #{rdnmAdr})")
    void insertStoreInfo(StoreInfoDTO storeInfoDTO);
}