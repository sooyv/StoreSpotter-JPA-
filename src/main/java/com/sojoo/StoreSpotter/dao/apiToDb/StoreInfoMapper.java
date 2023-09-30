package com.sojoo.StoreSpotter.dao.apiToDb;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreInfoMapper {
//    public List<StoreInfo> insertAll();
    void storeinfoAdd(StoreInfo storeInfo);
}