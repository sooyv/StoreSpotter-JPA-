package com.sojoo.StoreSpotter.service.storePair;

import com.sojoo.StoreSpotter.dao.storePair.DataPairMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.dto.storePair.PairData;
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
                String st_nm = convenienceData.getBizes_nm();
                String coordinates = convenienceData.getCoordinates();
                Integer region = convenienceData.getRegion_fk();
                distanceSphere(st_nm, coordinates, region);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void distanceSphere(String st_nm, String coordinates, Integer region) throws Exception {
//        System.out.println(dataPairMapper.distanceSphere(st_nm, coordinates, region));

        try {
            List<PairData> pairDataList = dataPairMapper.distanceSphere(st_nm, coordinates, region);
            int count = 0;
            for (PairData pairData : pairDataList) {
                System.out.println("지역명: " + pairData.getRegion_fk());
                System.out.println("기준좌표 상가명: " + pairData.getSt_nm());
                System.out.println("기준좌표" + pairData.getSt_coor());
                System.out.println("대상좌표 상가명: " + pairData.getCom_nm());
                System.out.println("대상좌표" + pairData.getCom_coor());
                System.out.println("비교거리" + pairData.getDist());
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        PairData pairData = new PairData();

//        dataPairMapper.insertConveniencePairTable(pairData);
    }



}



