package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
//import org.springframework.data.geo.Point;

import javax.persistence.*;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@SuperBuilder
public abstract class StoreInfo {
    @Id
    @Column(name = "bizes_id", nullable = false)
    private String bizesId;

    @Column(name = "bizes_nm", nullable = false)
    private String bizesNm;

<<<<<<< HEAD
    @Column(name="coordinates", columnDefinition = "Geometry")

=======
    @Column(name="coordinates", columnDefinition = "point", nullable = false)
>>>>>>> 0ffc609f808c3114ee9584847f628cd49f20ef61
    private Point coordinates;
//  private Geometry coordinates;
    @Column(name = "rdnm_adr", nullable = false)
    private String rdnmAdr;

    @Column(name = "region_fk", nullable = false)
    private Integer regionFk;


//    @Builder
//    public StoreInfo(String bizes_id, String bizes_nm, String rdnm_adr, Point coordinates, Region region){
//        this.bizesId = bizes_id;
//        this.bizesNm = bizes_nm;
//        this.rdnmAdr = rdnm_adr;
//        this.coordinates = coordinates;
//        this.region = region;
//    }


<<<<<<< HEAD
    public void setCoordinates(Double lon, Double lat) {
//        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        GeometryFactory geometryFactory = new GeometryFactory();
=======
    public static Point setCoordinates(Double lon, Double lat) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
>>>>>>> 0ffc609f808c3114ee9584847f628cd49f20ef61
        Coordinate coordinate = new Coordinate(lon, lat);
        return geometryFactory.createPoint(coordinate);
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
