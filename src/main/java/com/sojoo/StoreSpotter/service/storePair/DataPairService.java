package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            for (StoreInfo storeInfo : convenienceDataList) {
//                System.out.println("편의점 데이터 ID: " + storeInfo.getBizes_id());
//                System.out.println("편의점 데이터 이름: " + storeInfo.getBizes_nm());
//                System.out.println("편의점 데이터 주소: " + storeInfo.getRdnm_adr());
                System.out.println("편의점 데이터 coors: " + storeInfo.getCoordinates());
//                System.out.println("편의점 데이터 region_fk: " + storeInfo.getRegion_fk());
                System.out.println("------");
            }


            List<StoreInfo> cafeDataList = dataPairMapper.selectCafeData();
            for (int i = 0; i < cafeDataList.size(); i++) {
                System.out.println("카페 데이터: " + cafeDataList.get(i));
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}



