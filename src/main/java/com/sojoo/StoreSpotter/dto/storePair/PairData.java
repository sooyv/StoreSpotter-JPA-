package com.sojoo.StoreSpotter.dto.storePair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Geometry;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairData {
    private Integer pair_id;
    private Geometry st_coor;
    private Geometry com_coor;
    private Double dist;
    private Integer region_fk;



}
