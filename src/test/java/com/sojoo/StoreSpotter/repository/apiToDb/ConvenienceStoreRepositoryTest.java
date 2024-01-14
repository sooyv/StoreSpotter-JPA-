package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConvenienceStoreRepositoryTest {

    @Autowired ConvenienceStoreRepository convenienceStoreRepository;
//    @Test
//    public void testStoreInfo(){
//        List<ConvenienceStore> ss = convenienceStoreRepository.findConvenienceStore();
//        for (ConvenienceStore s : ss){
//            System.out.println("result : " + s);
//        }
//
//    }
}