package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.RegionMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.dto.apiToDb.Region;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.apache.ibatis.jdbc.Null;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Optional;


@Service
public class StoreInfoService {

    private final StoreInfoMapper storeInfoMapper;
    private final IndustryMapper industryMapper;
    private final RegionMapper regionMapper;

    @Autowired
    public StoreInfoService(StoreInfoMapper storeInfoMapper, IndustryMapper industryMapper, RegionMapper regionMapper) {
        this.storeInfoMapper = storeInfoMapper;
        this.industryMapper = industryMapper;
        this.regionMapper = regionMapper;
    }


    // 업종 삭제 코드 - api 다시 받아오기 전 테이블 데이터 삭제
    public void deleteApiData() throws Exception {
        List<Industry> industryList = industryMapper.selectIndustryList();

        for (Industry industry : industryList) {
            String indust_id = industry.getIndust_id();
            storeInfoMapper.deleteIndustTable(indust_id);
        }

//        for (int i = 0; i < industryList.size(); i++) {
//            for (int j = 0; j < regionList.size(); j++) {
//                String indust_id = industryList.get(i).getIndust_id();
//                int region_id = regionList.get(j).getRegion_id();
//
//                storeInfoMapper.deleteIndustRegionTable(indust_id, region_id);
//            }
//        }
    }

    // 업종 저장 코드 - 업종별로 전지역 데이터 저장
    public List<Industry> industrySave() throws Exception {
        System.out.println("industrySave method start");
        long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기

        try {
            // 데이터 삭제 로직 동작
            deleteApiData();

            List<Industry> industry = industryMapper.selectIndustryList();      // 업종 id, name 담긴 industry list 받아오기
            for (Industry value : industry) {
                connectToApi(value);
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
    public void connectToApi(Industry industry) throws Exception {

        try {
            String indust_id = industry.getIndust_id();

            // 지역 가져오기
            List<Region> regions = regionMapper.selectRegionList();
//            System.out.println("지역명 확인: " + regions);                        // region 가져오기
            for (Region region : regions) {
                Integer region_id = region.getRegion_id();

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
                            "&indsSclsCd=" + indust_id;      // 업종 코드(industry_id) G20405, I21201

                    URL url = new URL(sb);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestProperty("Content-Type", "application/xml");
                    conn.setRequestMethod("GET");
                    conn.connect();

                    SAXBuilder builder = new SAXBuilder();
                    Document document = builder.build(conn.getInputStream());

                    // 페이지 개수 가져오기
                    Element root = document.getRootElement();
                    System.out.println("-----root Null 여부 : " + root);
                    Element body = root.getChild("body");

                    Element totalCount = null;
                    if (body != null){
                        totalCount = body.getChild("totalCount");
                    } else {
                        System.out.println("body null이 발생하여 해당 바퀴 종료하고 다음 바퀴로 이동");
                        continue;
                    }

                    System.out.println("industry: " + industry);
                    System.out.println("region : " + region);
                    System.out.println("pageNo : " + j);

                    assert totalCount != null;
                    int totalCountValue = Integer.parseInt(totalCount.getText());

                    // 재할당한 totalPageCount
                    // 페이지 개수 구하기
                    totalPageCount = (totalCountValue / 1000) + 1;

                    // for문으로 각페이지 데이터 저장하기
                    publicApiDataSave(document, indust_id, region_id);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("industryCity method exit");

    }

    // api 데이터 저장 로직
    public void publicApiDataSave(Document document, String indust_id, Integer region_id) throws DuplicateKeyException {
        try {
            Element root = document.getRootElement();
            Element body = root.getChild("body");
            Element items = body.getChild("items");
            List<Element> itemList = items.getChildren("item");

            for (Element item : itemList) {
                String bizes_id = item.getChildText("bizesId");
                String bizes_nm = item.getChildText("bizesNm");
                String rdnm_adr = item.getChildText("rdnmAdr");
                Double lon = Double.valueOf(item.getChildText("lon"));  // 경도(lon)
                Double lat = Double.valueOf(item.getChildText("lat"));  // 위도(lat)

                StoreInfo storeInfo = new StoreInfo();
                storeInfo.setBizes_id(bizes_id);
                storeInfo.setBizes_nm(bizes_nm);
                storeInfo.setRdnm_adr(rdnm_adr);
                storeInfo.setCoordinates(lon, lat);
                storeInfo.setRegion_fk(region_id);

                // DB에 저장하기
                storeInfoMapper.insertApiData(storeInfo, indust_id);
            }

        } catch (DuplicateKeyException e) {
            System.out.println("Duplicate data found: " + e.getMessage());
        }
    }
}