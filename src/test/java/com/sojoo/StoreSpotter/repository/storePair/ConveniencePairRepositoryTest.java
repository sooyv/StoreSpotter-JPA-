package com.sojoo.StoreSpotter.repository.storePair;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import com.sojoo.StoreSpotter.entity.storePair.DataRecommend;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConveniencePairRepositoryTest {

    @Autowired ConveniencePairRepository conveniencePairRepository;
    @Autowired ConvenienceStoreRepository convenienceStoreRepository;
    @Test
    public void pairTest(){
        List<ConvenienceStore> convs = convenienceStoreRepository.findAll();

        ConvenienceStore conv = convs.get(0);
        String st_nm = conv.getBizesNm();
        Point st_coor = conv.getCoordinates();
        Integer region_fk = conv.getRegionFk();


        List<StoreInfoProjection> stores = conveniencePairRepository.convenience_distanceSphere(st_nm, st_coor, region_fk);
        for (StoreInfoProjection store : stores){
            System.out.println("StNm: " + store.getStNm());
            System.out.println("StCoor: " + store.getStCoor());
            System.out.println("ComNm: " + store.getComNm());
            System.out.println("ComCoor: " + store.getComCoor());
            System.out.println("Dist: " + store.getDist());
            System.out.println("RegionFk: " + store.getRegionFk());
        }

    }

//    @Test
//    public void RecommandByDistTest(){
//        List<DataRecommend> dataList = conveniencePairRepository.selectByDist("11", "200");
//        for(DataRecommend data : dataList){
//            System.out.println(data);
//        }
//    }

}