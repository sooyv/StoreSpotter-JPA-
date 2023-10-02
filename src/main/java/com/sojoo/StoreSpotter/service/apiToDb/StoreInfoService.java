package com.sojoo.StoreSpotter.service.apiToDb;

import com.sojoo.StoreSpotter.dao.apiToDb.StoreInfoMapper;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class StoreInfoService {
    private final StoreInfoMapper storeInfoMapper;

    @Autowired
    public StoreInfoService(StoreInfoMapper storeInfoMapper) {
        this.storeInfoMapper = storeInfoMapper;
    }

    public void saveStore() {
        try {
            // url 설정
            StringBuilder sb = new StringBuilder();
            sb.append("https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong?divId=ctprvnCd&type=xml");
            sb.append("&ServiceKey=%2BJTG2GnVWVXZAxaul97F7f9DHnabKZ5Oiaw5eMiZJ1jGKGxyPSNm89FrSrS9pq5%2FLD5DMiDRMT2JJFp6AnK9eQ%3D%3D");
            sb.append("&pageNo=" + 1);
            sb.append("&numOfRows=" + 3);
            sb.append("&indsSclsCd=" + "G20405");
            sb.append("&key=" + 41);

            // URL 연결
            URL url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setRequestMethod("get");
            conn.connect();

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(conn.getInputStream());


            Element root = (Element) document.getRootElement();
            Element body = (Element) root.getElementsByTagName("body");
            Element items = (Element) body.getElementsByTagName("items");
            NodeList item = items.getChildNodes();


            System.out.println("NOW LAW_CD = " + item);

//            for (Element element : item) {
//                ApartXmlParser apartXmlParser = transferXmlToParser(element);
//                System.out.println("apartXmlParser = " + apartXmlParser);
//            }
        }
        catch (Exception e){

        }
    }
}