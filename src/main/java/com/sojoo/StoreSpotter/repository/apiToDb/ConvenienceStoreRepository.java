package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConvenienceStoreRepository extends JpaRepository<ConvenienceStore, Long> {

}
