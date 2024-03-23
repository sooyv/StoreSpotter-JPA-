package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

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

    @Column(name="coordinates", columnDefinition = "point", nullable = false)
    private Point coordinates;

    @Column(name = "rdnm_adr", nullable = false)
    private String rdnmAdr;

    @Column(name = "region_fk", nullable = false)
    private Integer regionFk;


    public static Point setCoordinates(Double lon, Double lat) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
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
