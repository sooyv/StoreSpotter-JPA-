package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

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
            String apiUrl = "https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong?divId=ctprvnCd&key=41&indsSclsCd=G20405&divId=ctprvnCd&numOfRows=3&pageNo=1&type=xml&serviceKey=%2BJTG2GnVWVXZAxaul97F7f9DHnabKZ5Oiaw5eMiZJ1jGKGxyPSNm89FrSrS9pq5%2FLD5DMiDRMT2JJFp6AnK9eQ%3D%3D"; // 실제 API URL로 변경해야 합니다.

            // REST 요청을 보내고 응답을 StoreInfoDTO 배열로 받습니다.
            RestTemplate restTemplate = new RestTemplate();
            StoreInfoDTO[] response = restTemplate.getForObject(apiUrl, StoreInfoDTO[].class);
            System.out.println(response);

            // 받은 응답 데이터를 데이터베이스에 저장합니다.
            for (StoreInfoDTO storeInfoDTO : response) {
                storeInfoMapper.storeinfoAdd(storeInfoDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}