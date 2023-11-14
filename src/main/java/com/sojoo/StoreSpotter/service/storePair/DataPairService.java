package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
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

    @Autowired
    public DataPairService(DataPairMapper dataPairMapper) {
        this.dataPairMapper = dataPairMapper;
    }


    public void selectDataPair() throws Exception {
        try {
            long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기
            List<StoreInfo> convenienceDataList = dataPairMapper.selectConvenienceData();
            for (StoreInfo convenienceData : convenienceDataList) {
                String name = convenienceData.getBizes_nm();
                String point = convenienceData.getCoordinates();
                Integer region = convenienceData.getRegion_fk();
                distanceSphere(name, point, region);
            }
            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
            System.out.println("소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void distanceSphere(String name, String point, Integer region) {
        List<PairData> pairDataList = dataPairMapper.distanceSphere(name, point, region);
        for (PairData pair : pairDataList) {
            PairData pairdata = new PairData();
            pairdata.setSt_nm(pair.getSt_nm());
            pairdata.setSt_coor(pair.getSt_coor());
            pairdata.setCom_nm(pair.getCom_nm());
            pairdata.setCom_coor(pair.getCom_coor());
            pairdata.setDist(pair.getDist());
            pairdata.setRegion_fk(pair.getRegion_fk());
//            System.out.println(st_nm + " " + st_coor + " " + com_nm + " " + com_coor + " " + dist + " " + region_fk);
            insertPairData(pairdata);
        }
    }

    public void insertPairData(PairData pairData) {
        dataPairMapper.insertPairData(pairData);
    }


}



