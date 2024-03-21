package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.repository.storePair.CafePairRepository;
import com.sojoo.StoreSpotter.repository.storePair.ConveniencePairRepository;
import com.sojoo.StoreSpotter.repository.storePair.DataRecommandProjection;
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


    public List<DataRecommandProjection> selectPairByDist(String industId, String regionFk, String dist) {
        List<DataRecommandProjection> result = null;
        try{
            switch (industId) {
                case "G20405":
                    result = conveniencePairRepository.selectByDist(regionFk, dist);
                    break;

                case "I21201":
                    result =  cafePairRepository.selectByDist(regionFk, dist);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    // 지역별 평균거리
    public Double avgDistance(String industId, String regionFk) {

        System.out.println("받아오는값" + industId + regionFk);
        Double result = null;
        Integer region = Integer.parseInt(regionFk);
        try{
            switch (industId) {
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
