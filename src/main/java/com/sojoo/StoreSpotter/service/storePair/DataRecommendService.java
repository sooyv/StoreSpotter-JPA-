package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.DataRecommendNotFoundException;
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


    public List<DataRecommandProjection> selectPairByDist(String industryId, String regionFk, String dist) {
        List<DataRecommandProjection> result = null;
        try{
            switch (industryId) {
                case "G20405":
                    result = conveniencePairRepository.selectByDist(regionFk, dist);
                    break;

                case "I21201":
                    result =  cafePairRepository.selectByDist(regionFk, dist);
                    break;
            }
        }
        catch (Exception e){
            throw new DataRecommendNotFoundException(ErrorCode.DATA_RECOMMEND_NOT_FOUND);
        }

        return result;
    }

    public Double avgDistance(String industryId, String regionFk) {

        Double result = null;
        Integer region = Integer.parseInt(regionFk);
        try{
            switch (industryId) {
                case "G20405":
                    result = conveniencePairRepository.avgDist(region);
                    break;

                case "I21201":
                    result = cafePairRepository.avgDist(region);
                    break;
            }
        }
        catch (Exception e) {
            throw new DataRecommendNotFoundException(ErrorCode.DATA_RECOMMEND_NOT_FOUND);
        }
        return result;
    }

}