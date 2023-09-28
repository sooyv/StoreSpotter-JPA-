package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfoDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface StoreInfoMapper {
    public List<StoreInfoDTO> insertAll();
    void storeinfoAdd(StoreInfoDTO storeInfoDTO);
}