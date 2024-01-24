package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.repository.apiToDb.*;
import com.sojoo.StoreSpotter.entity.apiToDb.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Service
public class StoreInfoService {

    private final ConvenienceStoreRepository convenienceStoreRepository;
    private final CafeRepository cafeRepository;
    private final IndustryRepository industryRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public StoreInfoService(ConvenienceStoreRepository convenienceStoreRepository, CafeRepository cafeRepository, IndustryRepository industryRepository, RegionRepository regionRepository) {
        this.convenienceStoreRepository = convenienceStoreRepository;
        this.cafeRepository = cafeRepository;
        this.industryRepository = industryRepository;
        this.regionRepository = regionRepository;
    }



    // 업종 저장 코드 - 업종별로 전지역 데이터 저장
    public List<Industry> industrySave() throws Exception {
        long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

        try {
            // api 데이터 업데이트 전 기존 데이터 삭제
            convenienceStoreRepository.deleteAll();
            cafeRepository.deleteAll();
            //각 업종마다 api 데이터 받기
            List<Industry> industryList = industryRepository.findAll();
            for (Industry industry : industryList) {
                connectToApi(industry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime) / 1000; //두 시간에 차 계산
        System.out.println("소요시간 : " + secDiffTime/60 +"분 " + secDiffTime%60+"초");
        return null;
    }



    // 공공데이터 api 연결 및 Document 전달
    @Transactional
    public void connectToApi(Industry industry) throws Exception {

        try {
            String industId = industry.getIndustId();

            // 지역 가져오기
            List<Region> regions = regionRepository.findAll();
            for (Region region : regions) {
                Integer region_id = region.getRegionId();

                // 아래에서 totalPageCount 재할당
                int totalPageCount = 1;

                for (int j = 1; j <= totalPageCount; j++) {

                    // 해당 업종, 지역의 api 호출
                    String sb = "https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong?" +
                            "ServiceKey=kXVB%2FzGPSXqZrn%2F1NuCYPZGJONAmxZfu%2BjQDCfDP%2F5uo8QZ%2B6iWdY%2FXrV%2B0gg2z%2BMKVEA%2BrVFLs9l0TVQE2Cug%3D%3D" +
                            "&pageNo=" + j +
                            "&numOfRows=" + 1000 +
                            "&divId=" + "ctprvnCd" +
                            "&key=" + region_id +             // 시도 코드(region_id)
                            "&indsSclsCd=" + industId;      // 업종 코드(industry_id) G20405, I21201

                    URL url = new URL(sb);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestProperty("Content-Type", "application/xml");
                    conn.setRequestMethod("GET");
                    conn.connect();

                    SAXBuilder builder = new SAXBuilder();
                    Document document = builder.build(conn.getInputStream());

                    // 페이지 개수 가져오기
                    Element root = document.getRootElement();
<<<<<<< HEAD
=======
//                    System.out.println("-----root Null 여부 : " + root);
>>>>>>> 0ffc609f808c3114ee9584847f628cd49f20ef61
                    Element body = root.getChild("body");

                    Element totalCount = null;
                    if (body != null){
                        totalCount = body.getChild("totalCount");
                    } else {
                        System.out.println("body null이 발생하여 해당 바퀴 종료하고 다음 바퀴로 이동");
                        continue;
                    }

//                    System.out.println("industry: " + industry);
//                    System.out.println("region : " + region);
//                    System.out.println("pageNo : " + j);

                    assert totalCount != null;
                    int totalCountValue = Integer.parseInt(totalCount.getText());

                    // 재할당한 totalPageCount
                    // 페이지 개수 구하기
                    totalPageCount = (totalCountValue / 1000) + 1;

                    // for문으로 각페이지 데이터 저장하기
                    publicApiDataSave(document, industId, region_id);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("industryCity method exit");

    }

    // api 데이터 저장 로직
    public void publicApiDataSave(Document document, String industId, Integer regionId) throws DuplicateKeyException {
        try {
            Element root = document.getRootElement();
            Element body = root.getChild("body");
            Element items = body.getChild("items");
            List<Element> itemList = items.getChildren("item");

            for (Element item : itemList) {
                String bizesId = item.getChildText("bizesId");
                String bizesNm = item.getChildText("bizesNm");
                String rdnmAdr = item.getChildText("rdnmAdr");
                Double lon = Double.valueOf(item.getChildText("lon"));  // 경도(lon)
                Double lat = Double.valueOf(item.getChildText("lat"));  // 위도(lat)
                switch (industId){
                    case "G20405":
//                        ConvenienceStore convenienceStore = new ConvenienceStore();
//                        convenienceStore.setBizesId();
//                        convenienceStore.setBizesNm(bizes_nm);
//                        convenienceStore.setRdnmAdr(rdnm_adr);
//                        convenienceStore.setCoordinates(lon, lat);
//                        convenienceStore.setRegionFk(region_id);
//                        convenienceStoreRepository.save(convenienceStore);

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
//                        Cafe cafe = new Cafe();
//                        cafe.setBizesId();
//                        cafe.setBizesNm(bizes_nm);
//                        cafe.setRdnmAdr(rdnm_adr);
//                        cafe.setCoordinates(lon, lat);
//                        cafe.setRegionFk(region_id);
//                        cafeRepository.save(cafe);

<<<<<<< HEAD
                } else if (Objects.equals(indust_id, "I21201")) {
                    Cafe cafe = new Cafe();
                    cafe.setBizesId(bizes_id);
                    cafe.setBizesNm(bizes_nm);
                    cafe.setRdnmAdr(rdnm_adr);
                    cafe.setCoordinates(lat, lon);
                    cafe.setRegionFk(region_id);

                    cafeRepository.save(cafe);
=======
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
>>>>>>> 0ffc609f808c3114ee9584847f628cd49f20ef61
                }
            }

        } catch (DuplicateKeyException e) {
            System.out.println("Duplicate data found: " + e.getMessage());
        }
    }
}