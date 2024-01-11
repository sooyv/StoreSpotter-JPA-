package com.sojoo.StoreSpotter.entity.storePair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRecommend {
    private String st_nm;
    private String st_coor;
    private String com_nm;
    private String com_coor;
    private String dist;
    private String center_coor;
    private String pair_id;

}
