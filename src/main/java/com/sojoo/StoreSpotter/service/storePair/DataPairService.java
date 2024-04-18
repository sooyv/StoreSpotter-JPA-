package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.DataPairCreateFailedException;
import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
import com.sojoo.StoreSpotter.entity.storePair.PairData;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    @TimeTrace
    @Transactional
    public void saveIndustryPairData() {
        try {
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            conveniencePairRepository.deleteAll();
            cafePairRepository.deleteAll();
            System.out.println("saveIndustryPairData 여기 1");
            List<Industry> industryList = industryRepository.findAll();
            for (Industry industry : industryList) {
                String industId = industry.getIndustId();
                switch (industId) {
                    case "G20405":
                        List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAll();
                        selectDataPair(convenienceStoreList, industId);
                        break;
                    case "I21201":
                        List<Cafe> cafeList = cafeRepository.findAll();
                        selectDataPair(cafeList, industId);
                        break;
                }

                conveniencePairRepository.convenience_deleteDuplicatePairs();
                cafePairRepository.cafe_deleteDuplicatePairs();

                long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
                System.out.println(industry.getIndustName() + "Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");
            }
        } catch (Exception e) {
            throw new DataPairCreateFailedException(ErrorCode.DATA_PAIR_CREATE_FAILED);
        }
    }


    private <T extends StoreInfo> void selectDataPair(List<T> storeDataList, String industId) {
        List<ConveniencePair> conveniencePairs = new ArrayList<>();
        List<CafePair> cafePairs = new ArrayList<>();

        System.out.println("selectDataPair 여기 2");
        switch (industId) {
            case "G20405":
                for (StoreInfo storeData : storeDataList) {
                    System.out.println("selectDataPair : " + storeData + "확인");
                    String name = storeData.getBizesNm();
                    Point point = storeData.getCoordinates();
                    Integer region = storeData.getRegionFk();
                    ConveniencePair pairData = (ConveniencePair) distanceSphere(name, point, region, industId);
                    if (pairData != null) {
                        System.out.println("selectDataPair의 conv pair: " +pairData);
                        conveniencePairs.add(pairData);
                    }
                }
                conveniencePairRepository.saveAll(conveniencePairs);
                break;

            case "I21201":
                for (StoreInfo storeData : storeDataList) {
                    String name = storeData.getBizesNm();
                    Point point = storeData.getCoordinates();
                    Integer region = storeData.getRegionFk();
                    CafePair pairData = (CafePair) distanceSphere(name, point, region, industId);
                    if (pairData != null) {
                        System.out.println("selectDataPair의 cafe pair: " +pairData);
                        cafePairs.add(pairData);
                    }
                }
                cafePairRepository.saveAll(cafePairs);
                break;
        }
//        for (StoreInfo storeData : storeDataList) {
//            String name = storeData.getBizesNm();
//            Point point = storeData.getCoordinates();
//            Integer region = storeData.getRegionFk();
//        }
    }

    @TimeTrace
    private PairData distanceSphere(String name, Point point, Integer region, String industId) {
        System.out.println("distanceSphere 여기 3");
        switch (industId) {
            case "G20405":
                StoreInfoProjection convenienceProjection = conveniencePairRepository.convenience_distanceSphere(name, point, region);
                System.out.println("distanceSphere의 convProj 확인  : "+ convenienceProjection);

                    Point ConvStCoor = StoreInfo.createPointFromWkt(convenienceProjection.getStCoor());
                    Point convComCoor = StoreInfo.createPointFromWkt(convenienceProjection.getComCoor());
                    String convStNm = convenienceProjection.getStNm();
                    String convComNm = convenienceProjection.getComNm();
                    Double convDist = convenienceProjection.getDist();
                    Integer convRegionFk = convenienceProjection.getRegionFk();

                    return ConveniencePair.builder()
                                    .stNm(convStNm)
                                    .stCoor(ConvStCoor)
                                    .comNm(convComNm)
                                    .comCoor(convComCoor)
                                    .dist(convDist)
                                    .regionFk(convRegionFk)
                                    .build();
            case "I21201":
                StoreInfoProjection cafeProjection = cafePairRepository.cafe_distanceSphere(name, point, region);

                    Point cafeStCoor = StoreInfo.createPointFromWkt(cafeProjection.getStCoor());
                    Point cafeComCoor = StoreInfo.createPointFromWkt(cafeProjection.getComCoor());
                    String cafeStNm = cafeProjection.getStNm();
                    String cafeComNm = cafeProjection.getComNm();
                    Double cafeDist = cafeProjection.getDist();
                    Integer cafeRegionFk = cafeProjection.getRegionFk();

                    return CafePair.builder()
                                .stNm(cafeStNm)
                                .stCoor(cafeStCoor)
                                .comNm(cafeComNm)
                                .comCoor(cafeComCoor)
                                .dist(cafeDist)
                                .regionFk(cafeRegionFk)
                                .build();
        }
        return null;
    }

}



