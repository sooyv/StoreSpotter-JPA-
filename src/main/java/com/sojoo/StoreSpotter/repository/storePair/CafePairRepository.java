package com.sojoo.StoreSpotter.repository.storePair;

import com.sojoo.StoreSpotter.entity.storePair.CafePair;
import org.apache.ibatis.annotations.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CafePairRepository extends JpaRepository<CafePair, Integer> {
    @Query(value = "SELECT ST_DISTANCE_SPHERE(ST_GeomFromText(:st_coor, 4326), c.coordinates) AS dist," +
            "                       :region_fk AS region_fk," +
            "                       :st_nm AS st_nm," +
            "                       :st_coor AS st_coor," +
            "                       c.bizes_nm AS com_nm," +
            "                       ST_AsWKT(c.coordinates) AS com_coor" +
            "                FROM cafe c" +
            "                WHERE c.region_fk = :region_fk" +
            "                AND ST_Within(c.coordinates, ST_Buffer(ST_GeomFromText(:st_coor, 4326), 500))" +
            "                AND ST_Distance_Sphere(ST_GeomFromText(:st_coor, 4326), c.coordinates) > 10" +
            "                ORDER BY dist" +
            "                LIMIT 1", nativeQuery=true)
    List<CafePair> cafe_distanceSphere(@Param("st_nm") String st_nm, @Param("st_coor") Point st_coor, @Param("region_fk") Integer region_fk);


    @Modifying
    @Transactional
    @Query(value = "DELETE t1" +
            "                FROM cafe_pair t1" +
            "                JOIN cafe_pair t2" +
            "                ON t1.st_nm = t2.com_nm" +
            "                AND t1.com_nm = t2.st_nm" +
            "                AND t1.pair_id > t2.pair_id;", nativeQuery = true)
    void cafe_deleteDuplicatePairs();

    @Query(value = "SELECT c.st_nm AS stNm," +
            "                ST_AsText(c.st_coor) AS stCoor," +
            "                c.com_nm AS comNm," +
            "                ST_AsText(c.com_coor) AS comCoor," +
            "                c.dist," +
            "                ST_AsText(ST_Centroid(LineString((c.st_coor), (c.com_coor)))) AS centerCoor," +
            "                c.pair_id AS pairId" +
            "                FROM cafe_pair c" +
            "                WHERE c.region_fk = :region_fk " +
            "                AND c.dist > :dist", nativeQuery = true)
    List<DataRecommandProjection> selectByDist(@Param("region_fk") String region_fk, @Param("dist") String dist);

    @Query("SELECT AVG(c.dist) AS dist" +
            "                FROM CafePair c" +
            "                WHERE c.regionFk = :region_fk")
    Double avgDist(@Param("region_fk") Integer region_fk);

}
