package com.sojoo.StoreSpotter.repository.apiToDb;

public interface StoreInfoProjection {

    String getStNm();
    String getStCoor();
    String getComNm();
    String getComCoor();
    Double getDist();
    Integer getRegionFk();

}
