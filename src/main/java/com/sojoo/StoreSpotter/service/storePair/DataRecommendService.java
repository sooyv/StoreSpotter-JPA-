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


    public List<DataRecommend> selectPairByDist(String indust_id, String region_fk, String dist) {
        return dataRecommendMapper.selectByDist(indust_id, region_fk, dist);
    }


    // 지역별 평균거리
    public Double avgDistance(String indust_id, String region_fk) {
        // 만약 업종이나 주소 선택을 하지 않고 검색을 눌렀을 경우
//        if (indust_id.isEmpty()) {
//
//        } else if (region_fk.isEmpty()) {
//
//        }
        return dataRecommendMapper.avgDist(indust_id, region_fk);
    }

}
