package com.sojoo.StoreSpotter.repository.storePair;

import com.sojoo.StoreSpotter.entity.storePair.ConveniencePair;
import org.apache.ibatis.annotations.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConveniencePairRepository extends JpaRepository<ConveniencePair, Long> {

    @Query(value = "        SELECT :st_nm as stNm, " +
            "                       ST_AsText(:st_coor) as stCoor, " +
            "                       c.bizes_nm as comNm, " +
            "                       ST_AsText(c.coordinates) as comCoor," +
            "                       ST_DISTANCE_SPHERE(:st_coor, c.coordinates) as dist," +
            "                       :region_fk as regionFk" +
            "                FROM convenience_store c" +
            "                WHERE c.region_fk = :region_fk" +
            "                AND ST_Within(c.coordinates, ST_Buffer(:st_coor, 500))" +
            "                AND ST_DISTANCE_SPHERE(:st_coor, c.coordinates) > 10" +
            "                ORDER BY dist" +
            "                LIMIT 1", nativeQuery=true)
    List<StoreInfoProjection> convenience_distanceSphere(@Param("st_nm") String st_nm, @Param("st_coor") Point st_coor, @Param("region_fk") Integer region_fk);

    @Modifying
    @Transactional
    @Query(value = "DELETE t1" +
            "                FROM convenience_pair t1" +
            "                JOIN convenience_pair t2" +
            "                ON t1.st_nm = t2.com_nm" +
            "                AND t1.com_nm = t2.st_nm" +
            "                AND t1.pair_id > t2.pair_id;", nativeQuery = true)
    void convenience_deleteDuplicatePairs();

    @Query(value = "SELECT c.st_nm AS stNm," +
            "                ST_AsText(c.st_coor) AS stCoor," +
            "                c.com_nm AS comNm," +
            "                ST_AsText(c.com_coor) AS comCoor," +
            "                c.dist," +
            "                ST_AsText(ST_Centroid(LineString((c.st_coor), (c.com_coor)))) AS centerCoor," +
            "                c.pair_id AS pairId" +
            "                FROM convenience_pair c" +
            "                WHERE c.region_fk = :region_fk " +
            "                AND c.dist > :dist", nativeQuery = true)
    List<DataRecommandProjection> selectByDist(@Param("region_fk") String region_fk, @Param("dist") String dist);

    @Query("SELECT AVG(c.dist) AS dist" +
            "                FROM ConveniencePair c" +
            "                WHERE c.regionFk = :region_fk")
    Double avgDist(@Param("region_fk") Integer region_fk);




}
