package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StoreInfoService {
    private final StoreInfoMapper storeInfoMapper;

    @Autowired
    public StoreInfoService(StoreInfoMapper storeInfoMapper) {
        this.storeInfoMapper = storeInfoMapper;
    }

    public void saveStoreInfoFromApiResponse() {
        try {
            // API 서버의 URL을 직접 지정합니다.
            String apiUrl = ""; // 실제 API URL로 변경해야 합니다.

            // REST 요청을 보내고 응답을 StoreInfoDTO 배열로 받습니다.
            RestTemplate restTemplate = new RestTemplate();
            StoreInfo[] response = restTemplate.getForObject(apiUrl, StoreInfo[].class);
            System.out.println(response);

            // 받은 응답 데이터를 데이터베이스에 저장합니다.
            for (StoreInfo storeInfo : response) {
                storeInfoMapper.storeinfoAdd(storeInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}