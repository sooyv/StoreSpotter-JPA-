package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import com.sojoo.StoreSpotter.entity.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.awt.*;
import java.lang.annotation.Repeatable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreInfoServiceTest {

    @Autowired
    private ConvenienceStoreRepository convenienceStoreRepository;
    @Test
    @Transactional
    public void api데이터저장() throws Exception {
        // given
        Double lon = Double.valueOf(126.975395770913);
        Double lat = Double.valueOf(37.5585276043331);
        org.locationtech.jts.geom.Point convPoint = StoreInfo.setCoordinates(lon, lat);

        String bizeId = "MA111111220800001877";
        String bizeName = "testName";
        String rdnmAdr = "testAddress";
        Integer regionId = 11;

        // when
        ConvenienceStore convenienceStore = ConvenienceStore.builder()
                .bizesId(bizeId)
                .bizesNm(bizeName)
                .rdnmAdr(rdnmAdr)
                .coordinates(convPoint)
                .regionFk(regionId)
                .build();
        ConvenienceStore saveConvenience = convenienceStoreRepository.save(convenienceStore);
        Optional<ConvenienceStore> findConvenience = convenienceStoreRepository.findById(saveConvenience.getBizesId());

//         then
        org.assertj.core.api.Assertions.assertThat(findConvenience.get().getBizesId()).isEqualTo(convenienceStore.getBizesId());
    }


}