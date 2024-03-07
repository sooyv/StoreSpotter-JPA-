package com.sojoo.StoreSpotter.repository.apiToDb;

import org.locationtech.jts.geom.Point;

public interface StoreInfoProjection {

    String getStNm();
    String getStCoor();
    String getComNm();
    String getComCoor();
    Double getDist();
    Integer getRegionFk();

}
