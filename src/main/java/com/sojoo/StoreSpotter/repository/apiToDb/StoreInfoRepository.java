package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.StoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreInfoRepository extends JpaRepository<StoreInfo, Long> {
}