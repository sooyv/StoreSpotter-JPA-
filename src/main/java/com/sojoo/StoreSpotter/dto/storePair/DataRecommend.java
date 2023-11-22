package com.sojoo.StoreSpotter.dto.storePair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRecommend {

    private String st_nm;
    private String com_nm;
    private String dist;
    private String center_coor;
    private String pair_id;

}
