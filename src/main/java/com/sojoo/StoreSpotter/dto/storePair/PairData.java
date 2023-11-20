package com.sojoo.StoreSpotter.dto.storePair;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairData {
//    private Integer pair_id;
    private String st_nm;
    private String st_coor;
    private String com_nm;
    private String com_coor;
    private Double dist;
    private Integer region_fk;

//    private Object pairData; // 'pairData' 속성 추가
//
//    public Object getPairData() {
//        return pairData;
//    }
//
//    public void setPairData(Object pairData) {
//        this.pairData = pairData;
//    }

}
