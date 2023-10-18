package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.controller.apiToDb.IndustryController;
import com.sojoo.StoreSpotter.dao.apiToDb.IndustryMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.RegionMapper;
import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import com.sojoo.StoreSpotter.dto.apiToDb.Industry;
import com.sojoo.StoreSpotter.dto.apiToDb.Region;
import com.sojoo.StoreSpotter.dto.apiToDb.StoreInfo;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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


    // 업종 저장 코드 - 업종별로 전지역 데이터 저장 가능
    public List<Industry> industrySave() throws Exception {
        System.out.println("service단 industrySave 진입");

        try {
            // 업종 id, name 담긴 industry list 받아오기
            List<Industry> industry = industryMapper.selectIndustry();
            for (int i = 0; i < industry.size(); i++) {
                industryCity(industry.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 받은 industry의 api를 호출하여 각 지역에
    public void industryCity(Industry industry) throws Exception {
        System.out.println("industryCity 메서드 진입");

        try {
            // 업종 하나씩 받기 - 매개변수로 받은 industry
//            System.out.println("업종명 확인: " + industry);
            String indust_id = industry.getIndust_id();

            // 지역 가져오기
            List<Region> regions = regionMapper.selectRegionList();
            System.out.println("지역명 확인: " + regions);                        // region 가져오기 성공
            for (int i = 0; i < regions.size(); i++) {
                Region region = regions.get(i);
                Integer region_id = region.getRegion_id();
                System.out.println("지역코드 확인: " + region_id);
                System.out.println("업종코드 확인" + indust_id);

                // 아래에서 totalPageCount 재할당
                int totalPageCount = 1;

                for (int j = 1; j <= totalPageCount; j++) {

                    // 해당 업종, 지역의 api 호출
                    StringBuilder sb = new StringBuilder();

                    sb.append("https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong?");
                    sb.append("ServiceKey=kXVB%2FzGPSXqZrn%2F1NuCYPZGJONAmxZfu%2BjQDCfDP%2F5uo8QZ%2B6iWdY%2FXrV%2B0gg2z%2BMKVEA%2BrVFLs9l0TVQE2Cug%3D%3D");
                    sb.append("&pageNo=" + j);
                    sb.append("&numOfRows=" + 1000);
                    sb.append("&divId=" + "ctprvnCd");
                    sb.append("&key=" + region_id);             // 시도 코드(region_id)
                    // ***현재 "&indsSclsCd=" + indust_id 로 하면 카페까지 store_info 테이블에 모두 저장.***
                    // ***업종 코드에 따라 테이블 구분 할 수 있도록 구현***
                    sb.append("&indsSclsCd=" + indust_id);      // 업종 코드(industry_id) G20405

                    URL url = new URL(sb.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestProperty("Content-Type", "application/xml");
                    conn.setRequestMethod("GET");
                    conn.connect();
                    System.out.println(conn.getContentLength());

                    SAXBuilder builder = new SAXBuilder();
                    Document document = builder.build(conn.getInputStream());
                    document.getRootElement();


                    // 페이지 개수 가져오기
                    Element root = document.getRootElement();
                    Element body = root.getChild("body");

                    Element totalCount = body.getChild("totalCount");

                    int totalCountValue = Integer.parseInt(totalCount.getText());
                    System.out.println("Total Count: " + totalCountValue);


                    // 재할당한 totalPageCount
                    // 페이지 개수 구하기
                    totalPageCount = (totalCountValue / 1000) + 1;
                    System.out.println("전체 페이지 개수 카운트" + totalPageCount);


                    // for문으로 각페이지 데이터 저장하기
                     publicApiDataSave(document);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("try-catch 구문 종료");
    }


    // api 데이터 저장 로직
    public void publicApiDataSave(Document document) throws Exception {
        try {
            Element root = document.getRootElement();
            Element body = root.getChild("body");
            Element items = body.getChild("items");
            List<Element> itemList = items.getChildren("item");

            for (Element item : itemList) {
                String bizesId = item.getChildText("bizesId");
                String bizesNm = item.getChildText("bizesNm");
                String rdnmAdr = item.getChildText("rdnmAdr");
                // 위도, 경도 추가

                System.out.println("bizesId: " + bizesId);
                System.out.println("bizesNm: " + bizesNm);
                System.out.println("rdnmAdr: " + rdnmAdr);

                // 업종명_도시명 table에 자동저장하게 만들기 - 수정
                StoreInfo storeInfo = new StoreInfo();
                storeInfo.setBizesId(bizesId);
                storeInfo.setBizesNm(bizesNm);
                storeInfo.setRdnmAdr(rdnmAdr);

                // database에 저장하기
                storeInfoMapper.insertIndustryData(storeInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

