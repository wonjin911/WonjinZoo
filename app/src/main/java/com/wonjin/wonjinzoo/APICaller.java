package com.wonjin.wonjinzoo;

import android.os.StrictMode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class APICaller {

    ArrayList <String> title_list = new ArrayList<String>();

    public String apiCall() {
        String xml = "";

    	/* for URL Connection */
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    	/* Weather API CALL */
        try{
            URL url = new URL("http://wonjin911.woobi.co.kr/CuteAnimals/main.html");
            URLConnection url_read = url.openConnection();

            InputStream is = url_read.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine()) != null){
                xml += line;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return xml;
    }

    public ArrayList<AnimalSet> parser(String xml){

        ArrayList<AnimalSet> animalset_list = new ArrayList<AnimalSet>();

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            Element root = document.getDocumentElement();
            NodeList itemlist = root.getElementsByTagName("item");

            for (int i = 0; i < itemlist.getLength(); i++){
                Node itemnode = itemlist.item(i);
                Element item = (Element) itemnode;

                NodeList idxlist = item.getElementsByTagName("idx");
                Element idxelement = (Element) idxlist.item(0);
                Node idxnode = idxelement.getFirstChild();
                int idx = Integer.parseInt(idxnode.getNodeValue());

                NodeList typelist = item.getElementsByTagName("type");
                Element typeelement = (Element) typelist.item(0);
                Node typenode = typeelement.getFirstChild();
                int type = Integer.parseInt(typenode.getNodeValue());

                NodeList imgurllist = item.getElementsByTagName("img_url");
                Element imgurlelement = (Element) imgurllist.item(0);
                Node imgurlnode = imgurlelement.getFirstChild();
                String img_url = imgurlnode.getNodeValue();

                NodeList detaillist = item.getElementsByTagName("detail");
                Element detailelement = (Element) detaillist.item(0);
                Node detailnode = detailelement.getFirstChild();
                String detail = detailnode.getNodeValue();

                AnimalSet as = new AnimalSet(idx, type, img_url, detail);
                animalset_list.add(as);
                title_list.add(img_url);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return animalset_list;
    }
}
