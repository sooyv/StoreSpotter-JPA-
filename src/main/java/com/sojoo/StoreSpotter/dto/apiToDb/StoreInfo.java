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
    private Geometry coordinates;
//    private Point coordinates;
    private Integer region_fk;

    // 위도(lat)와 경도(lon) 값을 받아서 coordinates 필드에 Point 객체로 저장
//    public void setCoordinates(Float lon, Float lat) {
//        this.coordinates = new Point(lon, lat); // 경도(lon), 위도(lat)
//    }

    public void setCoordinates(Double lon, Double lat) {
        GeometryFactory geometryFactory = new GeometryFactory();
        this.coordinates = geometryFactory.createPoint(new Coordinate(lon, lat));   // lon: x, lat: y
    }

}