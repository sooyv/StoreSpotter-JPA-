package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.ApiDataNotFoundException;
import com.sojoo.StoreSpotter.config.timeTrace.TimeTrace;
import com.sojoo.StoreSpotter.repository.apiToDb.*;
import com.sojoo.StoreSpotter.entity.apiToDb.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class StoreInfoService {

    private final ConvenienceStoreRepository convenienceStoreRepository;
    private final CafeRepository cafeRepository;
    private final IndustryRepository industryRepository;
    private final RegionRepository regionRepository;
    private final String apiServiceKey;

    @Autowired
    public StoreInfoService(ConvenienceStoreRepository convenienceStoreRepository, CafeRepository cafeRepository,
                            IndustryRepository industryRepository, RegionRepository regionRepository,
                            @Value("${api.apiServiceKey}") String apiServiceKey) {
        this.convenienceStoreRepository = convenienceStoreRepository;
        this.cafeRepository = cafeRepository;
        this.industryRepository = industryRepository;
        this.regionRepository = regionRepository;
        this.apiServiceKey = apiServiceKey;
    }

    @Transactional
    @TimeTrace
    // 업종 저장 코드 - 업종별로 전지역 데이터 저장
    public void apiToDb() throws ApiDataNotFoundException {
        long beforeTime = System.currentTimeMillis();   // 코드 실행 전에 시간 받아오기

        try {
            convenienceStoreRepository.deleteAll();
            cafeRepository.deleteAll();

            List<Industry> industryList = industryRepository.findAll();
            for (Industry industry : industryList) {
                connectToApi(industry);
            }
        } catch (Exception e) {
            throw new ApiDataNotFoundException(ErrorCode.API_DATA_NOT_FOUND);
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime) / 1000; // 두 시간에 차 계산
        System.out.println("소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");
    }



    // 공공데이터 api 연결 및 Document 전달
    private void connectToApi(Industry industry) throws Exception {

        try {
            String industId = industry.getIndustId();

            List<Region> regionList = regionRepository.findAll();
            for (Region region : regionList) {
                Integer regionId = region.getRegionId();

                int totalPageCount = 1;

                for (int j = 1; j <= totalPageCount; j++) {

                    String sb = "https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong?" +
                            "ServiceKey=" + apiServiceKey +
                            "&pageNo=" + j +
                            "&numOfRows=" + 1000 +
                            "&divId=" + "ctprvnCd" +
                            "&key=" + regionId +
                            "&indsSclsCd=" + industId;

                    URL url = new URL(sb);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestProperty("Content-Type", "application/xml");
                    conn.setRequestMethod("GET");
                    conn.connect();

                    SAXBuilder builder = new SAXBuilder();
                    Document document = builder.build(conn.getInputStream());

                    Element root = document.getRootElement();
                    Element body = root.getChild("body");

                    Element totalCount;

                    if (body == null & totalPageCount == 1) {
                        throw new Exception();
                    }
                    if (body == null){
                        continue;
                    }

                    totalCount = body.getChild("totalCount");
                    int totalCountValue = Integer.parseInt(totalCount.getText());

                    totalPageCount = (totalCountValue / 1000) + 1;

                    publicApiDataSave(document, industId, regionId);
                }
            }

        } catch (Exception e) {
            throw new Exception();
        }
    }

    // api 데이터 저장 로직
    private void publicApiDataSave(Document document, String industId, Integer regionId) throws DuplicateKeyException {
            Element root = document.getRootElement();
            Element body = root.getChild("body");
            Element items = body.getChild("items");
            List<Element> itemList = items.getChildren("item");

            for (Element item : itemList) {
                String bizesId = item.getChildText("bizesId");
                String bizesNm = item.getChildText("bizesNm");
                String rdnmAdr = item.getChildText("rdnmAdr");
                Double lon = Double.valueOf(item.getChildText("lon"));
                Double lat = Double.valueOf(item.getChildText("lat"));
                try {
                    switch (industId){
                        case "G20405":
                                Point convPoint = StoreInfo.setCoordinates(lon, lat);
                                ConvenienceStore convenienceStore = ConvenienceStore.builder()
                                        .bizesId(bizesId)
                                        .bizesNm(bizesNm)
                                        .rdnmAdr(rdnmAdr)
                                        .coordinates(convPoint)
                                        .regionFk(regionId)
                                        .build();
                                convenienceStoreRepository.save(convenienceStore);
                            break;

                        case "I21201":
                            Point cafePoint = StoreInfo.setCoordinates(lon, lat);
                            Cafe cafe = Cafe.builder()
                                    .bizesId(bizesId)
                                    .bizesNm(bizesNm)
                                    .rdnmAdr(rdnmAdr)
                                    .coordinates(cafePoint)
                                    .regionFk(regionId)
                                    .build();
                            cafeRepository.save(cafe);
                            break;
                    }
                } catch (DuplicateKeyException e){
                    continue;
                }
        }
    }
}