package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, String> {
    Industry findAllByIndustName(String industName);

    Industry findOneByIndustId(String industId);
    List<Industry> findAll();
}
