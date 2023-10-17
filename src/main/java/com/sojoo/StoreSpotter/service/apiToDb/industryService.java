package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class industryService {
    private final IndustryMapper industryMapper;

    @Autowired
    public industryService(IndustryMapper industryMapper) {
        this.industryMapper = industryMapper;
    }

    // 업종 저장 코드 - 업종별로 전지역 데이터 저장 가능
//    public List<Industry> industrySave() throws Exception {
//        System.out.println("service단 industrySave 진입");
//
//        try {
//            // 업종 id, name 담긴 industry list 받아오기
//            List<Industry> industry = industryMapper.selectIndustry();
//            for (int i = 0; i < industry.size(); i++) {
//                industryCity(industry.get(i));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}
