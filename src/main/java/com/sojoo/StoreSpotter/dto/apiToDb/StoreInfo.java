package com.sojoo.StoreSpotter.dto.apiToDb;

import lombok.*;
//import org.springframework.data.geo.Point;
import org.locationtech.jts.geom.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfo {
    private String bizes_id;
    private String bizes_nm;
    private String rdnm_adr;
    private String coordinates;
//    private Geometry coordinates;
    private Integer region_fk;


    public void setCoordinates(Double lon, Double lat) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
        this.coordinates = String.valueOf(point);
    }

}
