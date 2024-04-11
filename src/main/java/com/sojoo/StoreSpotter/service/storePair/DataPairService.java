package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.DataPairCreateFailedException;
import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
import com.sojoo.StoreSpotter.repository.apiToDb.CafeRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.StoreInfoProjection;
import com.sojoo.StoreSpotter.repository.storePair.*;
import com.sojoo.StoreSpotter.entity.apiToDb.Cafe;
import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.entity.storePair.CafePair;
import com.sojoo.StoreSpotter.entity.storePair.ConveniencePair;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DataPairService {

    private final IndustryRepository industryRepository;
    private final ConvenienceStoreRepository convenienceStoreRepository;
    private final CafeRepository cafeRepository;
    private final ConveniencePairRepository conveniencePairRepository;
    private final CafePairRepository cafePairRepository;


    @Autowired
    public DataPairService(IndustryRepository industryRepository,
                           ConvenienceStoreRepository convenienceStoreRepository,
                           CafeRepository cafeRepository, ConveniencePairRepository conveniencePairRepository,
                           CafePairRepository cafePairRepository) {
        this.industryRepository = industryRepository;
        this.convenienceStoreRepository = convenienceStoreRepository;
        this.cafeRepository =  cafeRepository;
        this.conveniencePairRepository = conveniencePairRepository;
        this.cafePairRepository = cafePairRepository;
    }

    @Transactional
    @TimeTrace
    public void saveIndustryPairData() {
        try{
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            conveniencePairRepository.deleteAll();
            cafePairRepository.deleteAll();

            List<Industry> industryList = industryRepository.findAll();
            for (Industry industry : industryList){
                String industId = industry.getIndustId();
                switch (industId){
                    case "G20405":
                        List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAll();
                        selectDataPair(convenienceStoreList, industId);
                    case "I21201":
                        List<Cafe> cafeList = cafeRepository.findAll();
                        selectDataPair(cafeList, industId);
                }

                conveniencePairRepository.convenience_deleteDuplicatePairs();
                cafePairRepository.cafe_deleteDuplicatePairs();

                long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
                System.out.println(industry.getIndustName() + "Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");
            }
        }catch (Exception e){
            throw new DataPairCreateFailedException(ErrorCode.DATA_PAIR_CREATE_FAILED);
        }
    }

    @Transactional
    @TimeTrace
    public void saveConvIndustryPairData() {
        try{
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            conveniencePairRepository.deleteAll();

            List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAll();
            selectDataPair(convenienceStoreList, "G20405");

            conveniencePairRepository.convenience_deleteDuplicatePairs();

            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
            System.out.println("편의점 Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");

        }catch (Exception e){
            throw new DataPairCreateFailedException(ErrorCode.DATA_PAIR_CREATE_FAILED);
        }
    }

    @Transactional
    @TimeTrace
    public void saveCafeIndustryPairData() {
        try{
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            cafePairRepository.deleteAll();

            List<Cafe> cafeList = cafeRepository.findAll();
            selectDataPair(cafeList, "I21201");

            cafePairRepository.cafe_deleteDuplicatePairs();

            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
            System.out.println("카페 Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");

        }catch (Exception e){
            throw new DataPairCreateFailedException(ErrorCode.DATA_PAIR_CREATE_FAILED);
        }
    }


    private <T extends StoreInfo> void selectDataPair(List<T> storeDataList, String industId) {
        for (StoreInfo storeData : storeDataList) {
            String name = storeData.getBizesNm();
            Point point = storeData.getCoordinates();
            Integer region = storeData.getRegionFk();
            distanceSphere(name, point, region, industId);
        }
    }


    private void distanceSphere(String name, Point point, Integer region, String industId) {
        switch (industId){
            case "G20405":
                List<StoreInfoProjection> conveniencePairList = conveniencePairRepository.convenience_distanceSphere(name, point, region);
                for (StoreInfoProjection convenienceProjection : conveniencePairList){

                    Point stCoor = StoreInfo.createPointFromWkt(convenienceProjection.getStCoor());
                    Point comCoor = StoreInfo.createPointFromWkt(convenienceProjection.getComCoor());
                    String stNm = convenienceProjection.getStNm();
                    String comNm = convenienceProjection.getComNm();
                    Double dist = convenienceProjection.getDist();
                    Integer regionFk = convenienceProjection.getRegionFk();

                    ConveniencePair conveniencePair = ConveniencePair.builder()
                                    .stNm(stNm)
                                    .stCoor(stCoor)
                                    .comNm(comNm)
                                    .comCoor(comCoor)
                                    .dist(dist)
                                    .regionFk(regionFk)
                                    .build();

                    conveniencePairRepository.save(conveniencePair);
                }
            case "I21201":
                List<StoreInfoProjection> cafePairList = cafePairRepository.cafe_distanceSphere(name, point, region);
                for (StoreInfoProjection cafeProjection : cafePairList) {

                    Point stCoor = StoreInfo.createPointFromWkt(cafeProjection.getStCoor());
                    Point comCoor = StoreInfo.createPointFromWkt(cafeProjection.getComCoor());
                    String stNm = cafeProjection.getStNm();
                    String comNm = cafeProjection.getComNm();
                    Double dist = cafeProjection.getDist();
                    Integer regionFk = cafeProjection.getRegionFk();

                    CafePair cafePair = CafePair.builder()
                            .stNm(stNm)
                            .stCoor(stCoor)
                            .comNm(comNm)
                            .comCoor(comCoor)
                            .dist(dist)
                            .regionFk(regionFk)
                            .build();

                    cafePairRepository.save(cafePair);
                }
        }

    }

}



