package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndustryService {
    private final IndustryRepository industryRepository;

    @Autowired
    public IndustryService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public String getIndustryIdFromName(String industryName) {
        Industry industry = industryRepository.findByIndustName(industryName);
        System.out.println("industryService의 인더스트리 : " + industry.getIndustId());

        return industry.getIndustId();
    }
}
