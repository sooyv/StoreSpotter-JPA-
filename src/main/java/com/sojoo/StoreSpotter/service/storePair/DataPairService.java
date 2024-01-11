package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.repository.apiToDb.CafeRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.repository.storePair.CafePairRepository;
import com.sojoo.StoreSpotter.repository.storePair.ConveniencePairRepository;
import com.sojoo.StoreSpotter.repository.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.repository.storePair.DataPairRepository;
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

            List<Industry> industryList = industryRepository.findAll();
            for (Industry industry : industryList){
                String indust_id = industry.getIndustId();
                switch (indust_id){
                    case "G20405":
//                        List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAll();
//                        List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findConvenienceStore();
                        List<ConvenienceStore> convenienceStoreList = convenienceStoreRepository.findAll();

                        System.out.println("첫번째 좌표값 확인" + convenienceStoreList.get(0).getCoordinates());
                        System.out.println("첫번째 상가명 확인" + convenienceStoreList.get(0).getBizesNm());
                        System.out.println("save_industryPairData: " + convenienceStoreList);

                        selectDataPair(convenienceStoreList, indust_id);
                    case "I21201":
                        List<Cafe> cafeList = cafeRepository.findAll();
                        selectDataPair(cafeList, indust_id);
                }

//                List<StoreInfo> storeDataList = dataPairMapper.selectIndustryData(indust_id);
//                selectDataPair(storeDataList, indust_id);
//                dataPairMapper.deleteDuplicatePair(indust_id);
//                conveniencePairRepository.convenience_deleteDuplicatePairs();
//                cafePairRepository.cafe_deleteDuplicatePairs();

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
                System.out.println("selectDataPair name : "+ name);
                System.out.println("selectDataPair point : "+ point);
                System.out.println("selectDataPair region : "+ region);
                distanceSphere(name, point, region, indust_id);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void distanceSphere(String name, Point point, Integer region, String indust_id) {
//        List<PairData> pairDataList = dataPairMapper.distanceSphere(name, point, region, indust_id);
//        for (PairData pairdata : pairDataList) {
//            insertPairData(pairdata, indust_id);
//        }

        switch (indust_id) {
            case "G20405":
//                System.out.println("distanceSphere 메서드");
//                List<ConveniencePair> conveniencePairList = conveniencePairRepository.convenience_distanceSphere(name, point, region);
//                System.out.println("DataPairService 기준좌표 확인 : " + conveniencePairList.get(0).getStCoor());
//                System.out.println("DataPairService 기준상가명 확인 : " + conveniencePairList.get(0).getStNm());
//                conveniencePairRepository.saveAll(conveniencePairList);
                System.out.println("distanceSphere 메서드");
                List<ConveniencePair> conveniencePairList = conveniencePairRepository.convenience_distanceSphere(name, point, region);
                System.out.println("DataPairService 기준좌표 확인 : " + conveniencePairList.get(0).getStCoor());
                System.out.println("DataPairService 기준상가명 확인 : " + conveniencePairList.get(0).getStNm());

                conveniencePairRepository.saveAll(conveniencePairList);
            case "I21201":
                List<CafePair> cafePairList = cafePairRepository.cafe_distanceSphere(name, point, region);
                for (CafePair cafePair : cafePairList){
                    cafePairRepository.save(cafePair);
                }
//                cafePairRepository.saveAll(cafePairList);
        }

    }

//    public void insertPairData(PairData pairData, String indust_id) {
////        dataPairRepository.insertPairData(pairData, indust_id);
//        switch(indust_id){
//            case "G20405":
//                ConveniencePair conveniencePair = new ConveniencePair();
//                conveniencePair.setPairId(pairData.getPairId());
//                conveniencePair.setStNm(pairData.getStNm());
//                conveniencePair.setStCoor(pairData.getStCoor());
//                conveniencePair.setComNm(pairData.getComNm());
//                conveniencePair.setComCoor(pairData.getComCoor());
//                conveniencePair.setDist(pairData.getDist());
//                conveniencePair.setRegionFk(pairData.getRegionFk());
//                conveniencePairRepository.save(conveniencePair);
//                break;
//            case "I21201":
//                CafePair cafePair = new CafePair();
//                cafePair.setPairId(pairData.getPairId());
//                cafePair.setStNm(pairData.getStNm());
//                cafePair.setStCoor(pairData.getStCoor());
//                cafePair.setComNm(pairData.getComNm());
//                cafePair.setComCoor(pairData.getComCoor());
//                cafePair.setDist(pairData.getDist());
//                cafePair.setRegionFk(pairData.getRegionFk());
//                cafePairRepository.save(cafePair);
//                break;
//        }
//    }

}



