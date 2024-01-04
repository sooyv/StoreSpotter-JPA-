package com.sojoo.StoreSpotter.dao.storePair;


import com.sojoo.StoreSpotter.dto.storePair.PairData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataPairRepository extends JpaRepository<PairData, Integer> {

}
