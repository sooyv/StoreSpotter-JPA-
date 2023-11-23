package com.sojoo.StoreSpotter.service.storePair;

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

    public List<DataRecommend> selectPairByDist(String indust, String region, String dist) {
//        String indust_id = String.valueOf("G20405");
//        String region_fk = String.valueOf("11");
//        String distance = String.valueOf("200");
        return dataRecommendMapper.selectByDist(region, dist, indust);

    }

}
