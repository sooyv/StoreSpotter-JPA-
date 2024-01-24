package com.sojoo.StoreSpotter.repository.storePair;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ConveniencePairRepositoryTest {

    @Autowired ConveniencePairRepository conveniencePairRepository;
    @Autowired ConvenienceStoreRepository convenienceStoreRepository;
    @Test
    public void pairTest(){
        Optional<ConvenienceStore> conv = convenienceStoreRepository.findById("MA010120220800001877");

        System.out.println(conv);


//        List<StoreInfoProjection> stores = conveniencePairRepository.convenience_distanceSphere(st_nm, st_coor, region_fk);
//        for (StoreInfoProjection store : stores){
//            System.out.println("StNm: " + store.getStNm());
//            System.out.println("StCoor: " + store.getStCoor());
//            System.out.println("ComNm: " + store.getComNm());
//            System.out.println("ComCoor: " + store.getComCoor());
//            System.out.println("Dist: " + store.getDist());
//            System.out.println("RegionFk: " + store.getRegionFk());
//        }

    }

//    @Test
//    public void RecommandByDistTest(){
//        List<DataRecommend> dataList = conveniencePairRepository.selectByDist("11", "200");
//        for(DataRecommend data : dataList){
//            System.out.println(data);
//        }
//    }



}