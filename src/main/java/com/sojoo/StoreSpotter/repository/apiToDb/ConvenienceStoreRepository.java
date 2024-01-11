package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConvenienceStoreRepository extends JpaRepository<ConvenienceStore, Long> {

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO convenience_store (bizes_id, bizes_nm, rdnm_adr, coordinates, region_fk) " +
//            "VALUES (:#{#convenienceStore.bizesId}, :#{#convenienceStore.bizesNm}, " +
//            ":#{#convenienceStore.rdnmAdr}, ST_GeomFromText(:coordinates), :#{#convenienceStore.regionFk})", nativeQuery = true)
//    void insertConv(@Param("convenienceStore") ConvenienceStore convenienceStore, @Param("coordinates") String coordinates);

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO convenience_store (bizes_id, bizes_nm, rdnm_adr, coordinates, region_fk) " +
//            "VALUES (:#{#convenienceStore.bizesId}, :#{#convenienceStore.bizesNm}, " +
//            ":#{#convenienceStore.rdnmAdr}, :#{#convenienceStore.coordinates}, :#{#convenienceStore.regionFk})", nativeQuery = true)
//    void insertConv(@Param("convenienceStore") ConvenienceStore convenienceStore);

}
