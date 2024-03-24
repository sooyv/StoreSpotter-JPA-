package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.repository.apiToDb.IndustryRepository;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import org.springframework.stereotype.Service;

@Service
public class IndustryService {
    private final IndustryRepository industryRepository;

    public IndustryService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public String getIndustryIdFromName(String industryName) {
        Industry industry = industryRepository.findByIndustName(industryName);

        return industry.getIndustId();
    }
}
