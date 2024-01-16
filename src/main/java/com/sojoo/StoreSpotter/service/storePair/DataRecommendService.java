package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.repository.storePair.CafePairRepository;
import com.sojoo.StoreSpotter.repository.storePair.ConveniencePairRepository;
import com.sojoo.StoreSpotter.repository.storePair.DataRecommandProjection;
import com.sojoo.StoreSpotter.repository.storePair.DataRecommendMapper;
import com.sojoo.StoreSpotter.entity.storePair.DataRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataRecommendService {

    private final ConveniencePairRepository conveniencePairRepository;
    private final CafePairRepository cafePairRepository;


    @Autowired
    public DataRecommendService(ConveniencePairRepository conveniencePairRepository,
                                CafePairRepository cafePairRepository) {
        this.conveniencePairRepository = conveniencePairRepository;
        this.cafePairRepository = cafePairRepository;
    }


    public List<DataRecommandProjection> selectPairByDist(String indust_id, String region_fk, String dist) {
        List<DataRecommandProjection> result = null;
        try{
            switch (indust_id) {
                case "G20405":
                    result = conveniencePairRepository.selectByDist(region_fk, dist);
                    break;

                case "I21201":
                    result =  cafePairRepository.selectByDist(region_fk, dist);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    // 지역별 평균거리
    public Double avgDistance(String indust_id, String region_fk) {
        // 만약 업종이나 주소 선택을 하지 않고 검색을 눌렀을 경우
//        if (indust_id.isEmpty()) {
//
//        } else if (region_fk.isEmpty()) {
//
//        }
        System.out.println("받아오는값" + indust_id + region_fk);
        Double result = null;
        Integer region = Integer.parseInt(region_fk);
        try{
            switch (indust_id) {
                case "G20405":
                    result = conveniencePairRepository.avgDist(region);
                    break;

                case "I21201":
                    result = cafePairRepository.avgDist(region);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Dist : " + result);
        return result;
    }

}
