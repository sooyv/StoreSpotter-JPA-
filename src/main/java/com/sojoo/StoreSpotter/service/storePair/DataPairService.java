package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            List<StoreInfo> convenienceDataList = dataPairMapper.selectConvenienceData();
            for (StoreInfo convenienceData : convenienceDataList) {
                String point = convenienceData.getCoordinates();
                Integer region = convenienceData.getRegion_fk();
                distanceSphere(point, region);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void distanceSphere(String point, Integer region){
        System.out.println(dataPairMapper.distanceSphere(point, region));

    }


}



