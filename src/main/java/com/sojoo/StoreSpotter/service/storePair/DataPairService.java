package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.DataPairCreateFailedException;
import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
import com.sojoo.StoreSpotter.entity.apiToDb.*;
import com.sojoo.StoreSpotter.entity.storePair.PairData;
import com.sojoo.StoreSpotter.repository.apiToDb.*;
import com.sojoo.StoreSpotter.repository.storePair.*;
import com.sojoo.StoreSpotter.entity.storePair.CafePair;
import com.sojoo.StoreSpotter.entity.storePair.ConveniencePair;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataPairService {

    private final IndustryRepository industryRepository;
    private final ConvenienceStoreRepository convenienceStoreRepository;
    private final CafeRepository cafeRepository;
    private final ConveniencePairRepository conveniencePairRepository;
    private final CafePairRepository cafePairRepository;
    private final RegionRepository regionRepository;


    @Autowired
    public DataPairService(IndustryRepository industryRepository,
                           ConvenienceStoreRepository convenienceStoreRepository,
                           CafeRepository cafeRepository, ConveniencePairRepository conveniencePairRepository,
                           CafePairRepository cafePairRepository, RegionRepository regionRepository) {
        this.industryRepository = industryRepository;
        this.convenienceStoreRepository = convenienceStoreRepository;
        this.cafeRepository =  cafeRepository;
        this.conveniencePairRepository = conveniencePairRepository;
        this.cafePairRepository = cafePairRepository;
        this.regionRepository = regionRepository;
    }

    @TimeTrace
    @Transactional
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

            byRegion("G20405");

            conveniencePairRepository.convenience_deleteDuplicatePairs();

            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            double secDiffTime = (afterTime - beforeTime) / 1000.0; //두 시간에 차 계산
            System.out.println("편의점 Pair 생성 소요시간 : " + secDiffTime+"s");

        }catch (Exception e){
            System.out.println("Exception is this : " + Arrays.toString(e.getStackTrace()));
            throw new DataPairCreateFailedException(ErrorCode.DATA_PAIR_CREATE_FAILED);
        }
    }

    @Transactional
    @TimeTrace
    public void saveCafeIndustryPairData() {
        try{
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            cafePairRepository.deleteAll();

            byRegion("I21201");

            cafePairRepository.cafe_deleteDuplicatePairs();

            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
            System.out.println("카페 Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");

        }catch (Exception e){
            throw new DataPairCreateFailedException(ErrorCode.DATA_PAIR_CREATE_FAILED);
        }
    }


    public void byRegion(String industId){
        List<Region> regionList = regionRepository.findAll();
        switch (industId) {
            case "G20405":
                for (Region region : regionList) {
                    long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

                    List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAllByRegionFk(region.getRegionId());

                    selectDataPair(convenienceStoreList, industId);
                    long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                    double secDiffTime = (afterTime - beforeTime) / 1000.0; //두 시간에 차 계산
                    System.out.println(region.getRegionName() + " 편의점 Pair 저장 소요시간 : " + secDiffTime + "s");
                }
                break;
            case "I21201":
                for (Region region : regionList) {
                    long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

                    List<Cafe> cafeList = cafeRepository.findAllByRegionFk(region.getRegionId());

                    selectDataPair(cafeList, industId);
                    long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                    double secDiffTime = (afterTime - beforeTime) / 1000.0; //두 시간에 차 계산
                    System.out.println(region.getRegionName() + " 카페 Pair 저장 소요시간 : " + secDiffTime + "s");
                }
                break;
        }
    }


    private <T extends StoreInfo> void selectDataPair(List<T> storeDataList, String industId) {
        List<ConveniencePair> conveniencePairs = new ArrayList<>();
        List<CafePair> cafePairs = new ArrayList<>();

        switch (industId){
            case "G20405":
                for (StoreInfo storeData : storeDataList) {
                    String name = storeData.getBizesNm();
                    Point point = storeData.getCoordinates();
                    Integer region = storeData.getRegionFk();
                    ConveniencePair pair = convDistanceSphere(name, point, region);
                    if (pair != null) {
                        conveniencePairs.add(pair);
                    }
                }

                conveniencePairRepository.saveAll(conveniencePairs);
                break;

            case "I21201":
                for (StoreInfo storeData : storeDataList) {
                    String name = storeData.getBizesNm();
                    Point point = storeData.getCoordinates();
                    Integer region = storeData.getRegionFk();
                    CafePair pair = cafeDistanceSphere(name, point, region);
                    if (pair != null) {
                        cafePairs.add(pair);
                    }
                }

                cafePairRepository.saveAll(cafePairs);
                break;
        }


    }

    private ConveniencePair convDistanceSphere(String name, Point point, Integer region) {
        Optional<StoreInfoProjection> conveniencePairOptional = conveniencePairRepository.convenience_distanceSphere(name, point, region);
        if (conveniencePairOptional.isPresent()) {

            StoreInfoProjection conveniencePair = conveniencePairOptional.get();
            String convPairId = conveniencePair.getPairId();
            Point convStCoor = StoreInfo.createPointFromWkt(conveniencePair.getStCoor());
            Point convComCoor = StoreInfo.createPointFromWkt(conveniencePair.getComCoor());
            String convStNm = conveniencePair.getStNm();
            String convComNm = conveniencePair.getComNm();
            Double convDist = conveniencePair.getDist();
            Integer convRegionFk = conveniencePair.getRegionFk();

            return ConveniencePair.builder()
                    .pairId(convPairId)
                    .stNm(convStNm)
                    .stCoor(convStCoor)
                    .comNm(convComNm)
                    .comCoor(convComCoor)
                    .dist(convDist)
                    .regionFk(convRegionFk)
                    .build();
        } else {
            return null;
        }
    }

    private CafePair cafeDistanceSphere(String name, Point point, Integer region){
        Optional<StoreInfoProjection> cafePairOptional = cafePairRepository.cafe_distanceSphere(name, point, region);

        if (cafePairOptional.isPresent()) {

            StoreInfoProjection cafePair = cafePairOptional.get();
            String cafePairId = cafePair.getPairId();
            Point cafeStCoor = StoreInfo.createPointFromWkt(cafePair.getStCoor());
            Point cafeComCoor = StoreInfo.createPointFromWkt(cafePair.getComCoor());
            String cafeStNm = cafePair.getStNm();
            String cafeComNm = cafePair.getComNm();
            Double cafeDist = cafePair.getDist();
            Integer cafeRegionFk = cafePair.getRegionFk();

            return CafePair.builder()
                    .pairId(cafePairId)
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