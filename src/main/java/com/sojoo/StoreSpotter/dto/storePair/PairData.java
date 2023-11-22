package com.sojoo.StoreSpotter.dto.storePair;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Geometry;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairData {
    private String st_nm;
    private String st_coor;
    private String com_nm;
    private String com_coor;
    private Double dist;
    private Integer region_fk;

//    private Object pairData; // 'pairData' 속성 추가

//    public Object getPairData() {
//        return pairData;
//    }
//    public void setPairData(Object pairData) {
//        this.pairData = pairData;
//    }
}
