package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
//import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class StoreInfo {
    @Id
    @Column(name = "bizes_id")
    private String bizesId;

    @Column(name = "bizes_nm")
    private String bizesNm;

    @Column(name="coordinates", columnDefinition = "Point")
    private Point coordinates;
//        private Geometry coordinates;

    @Column(name = "rdnm_adr")
    private String rdnmAdr;

    @Column(name = "region_fk")
    private Integer regionFk;

//    @Builder
//    public StoreInfo(String bizes_id, String bizes_nm, String rdnm_adr, String coordinates, Integer region_fk){
//        this.bizesId = bizes_id;
//        this.bizesNm = bizes_nm;
//        this.rdnmAdr = rdnm_adr;
//        this.coordinates = coordinates;
//        this.regionFk = region_fk;
//    }


    public void setCoordinates(Double lon, Double lat) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(lon, lat);
        System.out.println(geometryFactory.createPoint(new Coordinate(lon, lat)));
        this.coordinates = geometryFactory.createPoint(coordinate);
    }

}
