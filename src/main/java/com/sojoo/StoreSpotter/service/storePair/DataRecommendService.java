package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dao.storePair.DataRecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataRecommendService {

    private final DataRecommendMapper dataRecommendMapper;

    @Autowired
    public DataRecommendService(DataRecommendMapper dataRecommendMapper){
        this.dataRecommendMapper = dataRecommendMapper;
    }

    public void selectPairByDist(String region, String dist, String indust) {
//        String indust_id = String.valueOf("G20405");
//        String region_fk = String.valueOf("11");
//        String dist = String.valueOf("200");
        dataRecommendMapper.selectByDist(region, dist, indust);
    }

}
