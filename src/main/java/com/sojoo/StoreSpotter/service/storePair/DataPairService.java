package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DataPairService {

    private final DataPairMapper dataPairMapper;
    private final IndustryMapper industryMapper;


    @Autowired
    public DataPairService(DataPairMapper dataPairMapper, IndustryMapper industryMapper) {
        this.dataPairMapper = dataPairMapper;
        this.industryMapper = industryMapper;
    }


    public void SavePairData() throws Exception {
        try {
            List<Industry> industryList = industryMapper.selectIndustryList();
            for (Industry industry : industryList) {
                String indust_id = industry.getIndust_id();
                List<StoreInfo> storeInfoData = dataPairMapper.selectIndustryData(indust_id);
                selectDataPair(storeInfoData, indust_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectDataPair(List<StoreInfo> storeDataList, String indust_id) {
        for (StoreInfo storeData : storeDataList) {
            String st_nm = storeData.getBizes_nm();
            String coordinates = storeData.getCoordinates();
            Integer region_fk = storeData.getRegion_fk();

            distanceSphere(st_nm, coordinates, region_fk, indust_id);
        }
    }


    public void distanceSphere(String st_nm, String coordinates, Integer region_fk, String indust_id) {
        List<PairData> pairDataList = dataPairMapper.distanceSphere(st_nm, coordinates, region_fk, indust_id);
        for (PairData pairData : pairDataList) {
            insertPairData(pairData, indust_id);
        }
    }


    public void insertPairData(PairData pairData, String indust_id) {
        System.out.println("insert 중! : " + pairData);
        dataPairMapper.insertPairData(pairData, indust_id);
    }

}



