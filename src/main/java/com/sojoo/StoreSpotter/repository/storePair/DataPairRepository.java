package com.sojoo.StoreSpotter.repository.storePair;


import com.sojoo.StoreSpotter.entity.storePair.PairData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPairRepository extends JpaRepository<PairData, Integer> {

}
