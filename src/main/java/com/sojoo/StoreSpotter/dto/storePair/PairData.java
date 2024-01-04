package com.sojoo.StoreSpotter.dto.storePair;

import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Geometry;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class PairData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pair_id")
    private Long pairId;

    @Column(name = "st_nm")
    private String stNm;

    @Column(name = "st_coor")
    private String stCoor;

    @Column(name = "com_nm")
    private String comNm;

    @Column(name = "com_coor")
    private String comCoor;

    @Column(name = "dist")
    private Double dist;

    @Column(name = "region_fk")
    private Integer regionFk;


//    @Builder
//    public PairData(Long pairId, String stNm, String stCoor, String comNm, String comCoor, Double dist, Integer regionFk){
//        this.pairId = pairId;
//        this.stNm = stNm;
//        this.stCoor = stCoor;
//        this.comNm = comNm;
//        this.comCoor = comCoor;
//        this.dist = dist;
//        this.regionFk = regionFk;
//    }
}
