package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.repository.apiToDb.CafeRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
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

import java.util.List;
import java.util.Optional;

@Service
public class DataPairService {

    private final DataPairMapper dataPairMapper;
    private final DataPairRepository dataPairRepository;
    private final IndustryRepository industryRepository;
    private final ConvenienceStoreRepository convenienceStoreRepository;
    private final CafeRepository cafeRepository;
    private final ConveniencePairRepository conveniencePairRepository;
    private final CafePairRepository cafePairRepository;


    @Autowired
    public DataPairService(DataPairMapper dataPairMapper, DataPairRepository dataPairRepository,
                           IndustryRepository industryRepository,
                           ConvenienceStoreRepository convenienceStoreRepository,
                           CafeRepository cafeRepository, ConveniencePairRepository conveniencePairRepository,
                           CafePairRepository cafePairRepository) {
        this.dataPairMapper = dataPairMapper;
        this.dataPairRepository = dataPairRepository;
        this.industryRepository = industryRepository;
        this.convenienceStoreRepository = convenienceStoreRepository;
        this.cafeRepository =  cafeRepository;
        this.conveniencePairRepository = conveniencePairRepository;
        this.cafePairRepository = cafePairRepository;
    }

//    @Transactional
    public void save_industryPairData() throws Exception{
        try{
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            conveniencePairRepository.deleteAll();
            cafePairRepository.deleteAll();

            List<Industry> industryList = industryRepository.findAll();
            for (Industry industry : industryList){
                String indust_id = industry.getIndustId();
                switch (indust_id){
                    case "G20405":
                        List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAll();
                        selectDataPair(convenienceStoreList, indust_id);
                    case "I21201":
                        List<Cafe> cafeList = cafeRepository.findAll();
                        selectDataPair(cafeList, indust_id);
                }

                conveniencePairRepository.convenience_deleteDuplicatePairs();
                cafePairRepository.cafe_deleteDuplicatePairs();

                long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
                System.out.println(industry.getIndustName() + "Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public <T extends StoreInfo> void selectDataPair(List<T> storeDataList, String indust_id) throws Exception {
        try {
            for (StoreInfo storeData : storeDataList) {
                String name = storeData.getBizesNm();
                Point point = storeData.getCoordinates();
                Integer region = storeData.getRegionFk();
                distanceSphere(name, point, region, indust_id);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void distanceSphere(String name, Point point, Integer region, String indust_id) {

        System.out.println(name + point + region);
        switch (indust_id){
            case "G20405":
                List<StoreInfoProjection> conveniencePairList = conveniencePairRepository.convenience_distanceSphere(name, point, region);
                for (StoreInfoProjection convenienceProjection : conveniencePairList){

                    Point stCoor = StoreInfo.createPointFromWkt(convenienceProjection.getStCoor());
                    Point comCoor = StoreInfo.createPointFromWkt(convenienceProjection.getComCoor());

                    ConveniencePair conveniencePair = new ConveniencePair();
                    conveniencePair.setStNm(convenienceProjection.getStNm());
                    conveniencePair.setStCoor(stCoor);
                    conveniencePair.setComNm(convenienceProjection.getComNm());
                    conveniencePair.setComCoor(comCoor);
                    conveniencePair.setDist(convenienceProjection.getDist());
                    conveniencePair.setRegionFk(convenienceProjection.getRegionFk());

                    conveniencePairRepository.save(conveniencePair);
                }
            case "I21201":
                List<StoreInfoProjection> cafePairList = conveniencePairRepository.convenience_distanceSphere(name, point, region);
                for (StoreInfoProjection cafeProjection : cafePairList) {

                    Point stCoor = StoreInfo.createPointFromWkt(cafeProjection.getStCoor());
                    Point comCoor = StoreInfo.createPointFromWkt(cafeProjection.getComCoor());

                    CafePair cafePair = new CafePair();
                    cafePair.setStNm(cafeProjection.getStNm());
                    cafePair.setStCoor(stCoor);
                    cafePair.setComNm(cafeProjection.getComNm());
                    cafePair.setComCoor(comCoor);
                    cafePair.setDist(cafeProjection.getDist());
                    cafePair.setRegionFk(cafeProjection.getRegionFk());

                    cafePairRepository.save(cafePair);
                }
        }

    }

}



