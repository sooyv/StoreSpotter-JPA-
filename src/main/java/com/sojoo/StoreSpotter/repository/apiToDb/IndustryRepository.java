package com.sojoo.StoreSpotter.repository.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
<<<<<<< HEAD
public interface IndustryRepository extends JpaRepository<Industry, Long> {
=======
public interface IndustryRepository extends JpaRepository<Industry, String> {
>>>>>>> 8d21aa2893f887c2d3bd61f0c27ffdb44a7b4e98
    Industry findAllByIndustName(String industName);

    Industry findOneByIndustId(String industId);
    List<Industry> findAll();
}
