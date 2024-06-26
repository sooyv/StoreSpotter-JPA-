package com.sojoo.StoreSpotter.entity.storePair;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@SuperBuilder
public abstract class PairData {

    @Id
    @Column(name = "pair_id", nullable = false)
    private String pairId;

    @Column(name = "st_nm", nullable = false)
    private String stNm;

    @Column(name = "st_coor", columnDefinition = "geometry", nullable = false)
    private Point stCoor;

    @Column(name = "com_nm", nullable = false)
    private String comNm;

    @Column(name = "com_coor", columnDefinition = "geometry", nullable = false)
    private Point comCoor;

    @Column(name = "dist", nullable = false)
    private Double dist;

    @Column(name = "region_fk", nullable = false)
    private Integer regionFk;

}
