package com.sojoo.StoreSpotter.entity.apiToDb;

import com.sojoo.StoreSpotter.entity.storePair.PairData;
import lombok.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
//import org.springframework.data.geo.Point;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class StoreInfo {
    @Id
    @Column(name = "bizes_id")
    private String bizesId;

    @Column(name = "bizes_nm")
    private String bizesNm;

    @Column(name="coordinates", columnDefinition = "geometry")
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

    public static Point createPointFromWkt(String wktPoint) {
        WKTReader wktReader = new WKTReader();
        try {
            Geometry geometry = wktReader.read(wktPoint);
            if (geometry instanceof Point) {
                return (Point) geometry;
            } else {
                // Handle the case where the input is not a Point (optional)
                throw new IllegalArgumentException("Input is not a Point");
            }
        } catch (ParseException e) {
            // Handle parsing exception
            throw new IllegalArgumentException("Error parsing WKT", e);
        }
    }

}
