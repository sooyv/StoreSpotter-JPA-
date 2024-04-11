package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import com.sojoo.StoreSpotter.entity.apiToDb.StoreInfo;
import com.sojoo.StoreSpotter.repository.apiToDb.ConvenienceStoreRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = true)
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreInfoServiceTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ConvenienceStoreRepository convenienceStoreRepository;

    @Test
    @TimeTrace
    public void storeInfoStoreTest() {
        // given
        String bizesId = "testId";
        String bizesNm = "testNm";
        String rdnmAdr = "testAdr";
        Point convPoint = StoreInfo.setCoordinates(1.11, 2.22);
        Integer regionFk = 11;

        ConvenienceStore convenienceStore = ConvenienceStore.builder()
                .bizesId(bizesId)
                .bizesNm(bizesNm)
                .rdnmAdr(rdnmAdr)
                .coordinates(convPoint)
                .regionFk(regionFk)
                .build();

        // when
        convenienceStoreRepository.save(convenienceStore);
        entityManager.flush();

        assertThat(convenienceStore.getBizesNm()).isEqualTo(convenienceStoreRepository.findById("testId").get().getBizesNm());


    }
}
