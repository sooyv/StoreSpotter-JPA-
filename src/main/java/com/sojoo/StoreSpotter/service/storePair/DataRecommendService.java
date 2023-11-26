package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.RegionMapper;
import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dao.storePair.DataRecommendMapper;
import com.sojoo.StoreSpotter.dto.storePair.DataRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataRecommendService {

    private final DataRecommendMapper dataRecommendMapper;

    @Autowired
    public DataRecommendService(DataRecommendMapper dataRecommendMapper){
        this.dataRecommendMapper = dataRecommendMapper;
    }


    public List<DataRecommend> selectPairByDist(String indust_fk, String dist, String indust) {
//        String indust_id = String.valueOf("G20405");
//        String region_fk = String.valueOf("11");
//        String dist = String.valueOf("200");
        return dataRecommendMapper.selectByDist(indust_fk, dist, indust);
    }


    // 지역별 평균거리
    public Double avgDistance(String indust_id, String region_fk) {
        return dataRecommendMapper.avgDist(indust_id, region_fk);
    }

}
