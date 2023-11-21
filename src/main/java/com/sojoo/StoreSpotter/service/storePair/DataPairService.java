package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataPairService {

    private final DataPairMapper dataPairMapper;
    private final IndustryMapper industryMapper;


    @Autowired
    public DataPairService(DataPairMapper dataPairMapper, IndustryMapper industryMapper) {
        this.dataPairMapper = dataPairMapper;
        this.industryMapper = industryMapper;
    }

    public void save_industryPairData() throws Exception{
        try{
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

            List<Industry> industryList = industryMapper.selectIndustryList();
            for (Industry industry : industryList){
                String indust_id = industry.getIndust_id();
                List<StoreInfo> storeDataList = dataPairMapper.selectIndustryData(indust_id);
                selectDataPair(storeDataList, indust_id);
                System.out.println(industry.getIndust_name() + " delete duplicate");
                dataPairMapper.deleteDuplicatePair(indust_id);

                long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
                System.out.println(industry.getIndust_name() + "Pair 생성 소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void selectDataPair(List<StoreInfo> storeDataList, String indust_id) throws Exception {
        try {
            for (StoreInfo storeData : storeDataList) {
                String name = storeData.getBizes_nm();
                String point = storeData.getCoordinates();
                Integer region = storeData.getRegion_fk();
                distanceSphere(name, point, region, indust_id);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void distanceSphere(String name, String point, Integer region, String indust_id) {
        List<PairData> pairDataList = dataPairMapper.distanceSphere(name, point, region, indust_id);
        for (PairData data : pairDataList) {
            insertPairData(data, indust_id);
        }
    }

    public void insertPairData(PairData pairData, String indust_id) {
        dataPairMapper.insertPairData(pairData, indust_id);
    }


}



