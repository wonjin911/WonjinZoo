package com.wonjin.wonjinzoo;

import android.os.StrictMode;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class APICaller {

    ArrayList <String> title_list = new ArrayList<String>();

    public String apiCall(URL url) {
        String xml = "";

    	/* for URL Connection */
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    	/* API CALL */
        try{
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

    public String apiPost(String url, List<NameValuePair> urlParameters) {
        String xml = "";

    	/* for URL Connection */
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    	/* API CALL */
        try{

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            // add header
            post.setHeader("User-Agent", "Mozilla/5.0");
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "utf-8"));
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            xml = result.toString();
        }catch(Exception e){
            e.printStackTrace();
        }

        return xml;
    }

    public int user_login(String uuid) {
        int user_idx = 0;
        String xml = "";
        try {
            URL url = new URL("http://wonjin911.woobi.co.kr/CuteAnimals/user_login.html?uuid=" + uuid);
            xml = apiCall(url);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            Element root = document.getDocumentElement();
            Node nl = root.getElementsByTagName("user_idx").item(0).getFirstChild();
            user_idx = Integer.parseInt(nl.getNodeValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user_idx;
    }

    public int user_reg(String uuid, String user_name){
        int user_idx = 0;
        String xml = "";
        try{
            String url = "http://wonjin911.woobi.co.kr/CuteAnimals/user_reg.html";
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("uuid", uuid));
            urlParameters.add(new BasicNameValuePair("user_name", user_name));
            xml = apiPost(url, urlParameters);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            Element root = document.getDocumentElement();
            Node nl = root.getElementsByTagName("user_idx").item(0).getFirstChild();
            user_idx = Integer.parseInt(nl.getNodeValue());
        } catch (Exception e){
            e.printStackTrace();
        }
        return user_idx;
    }

    public int user_good(int user_idx, int animal_idx) {
        String xml = "";
        int ret = 0;
        try {
            URL url = new URL("http://wonjin911.woobi.co.kr/CuteAnimals/user_good_chk.html?user_idx=" + user_idx + "&animal_idx=" + animal_idx);
            xml = apiCall(url);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            Element root = document.getDocumentElement();
            Node nl = root.getElementsByTagName("cnt").item(0).getFirstChild();
            int cnt = Integer.parseInt(nl.getNodeValue());
            if (cnt == 0){
                url = new URL("http://wonjin911.woobi.co.kr/CuteAnimals/user_good_ins.html?user_idx=" + user_idx + "&animal_idx=" + animal_idx);
                apiCall(url);
                ret = 1;
            }else{
                ret = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ret;
    }

    public String getAnimalList(int idx, int type, int num){
        String xml = "";
        try {
            URL url = new URL("http://wonjin911.woobi.co.kr/CuteAnimals/main.html?idx=" + idx + "&type=" + type + "&num=" + num);
            xml = apiCall(url);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return xml;
    }

    public String getBestList(int type){
        String xml = "";
        try {
            URL url = new URL("http://wonjin911.woobi.co.kr/CuteAnimals/get_best.html?type=" + type);
            xml = apiCall(url);
        }catch(MalformedURLException e){
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
                Node item_node = itemlist.item(i);
                Element item = (Element) item_node;

                NodeList idx_list = item.getElementsByTagName("idx");
                Element idx_element = (Element) idx_list.item(0);
                Node idx_node = idx_element.getFirstChild();
                int idx = Integer.parseInt(idx_node.getNodeValue());

                NodeList type_list = item.getElementsByTagName("type");
                Element type_element = (Element) type_list.item(0);
                Node type_node = type_element.getFirstChild();
                int type = Integer.parseInt(type_node.getNodeValue());

                NodeList img_url_list = item.getElementsByTagName("img_url");
                Element img_url_element = (Element) img_url_list.item(0);
                Node img_url_node = img_url_element.getFirstChild();
                String img_url = img_url_node.getNodeValue();

                NodeList detail_list = item.getElementsByTagName("detail");
                Element detail_element = (Element) detail_list.item(0);
                Node detail_node = detail_element.getFirstChild();
                String detail = detail_node.getNodeValue();

                NodeList view_cnt_list = item.getElementsByTagName("view_cnt");
                Element view_cnt_element = (Element) view_cnt_list.item(0);
                Node view_cnt_node = view_cnt_element.getFirstChild();
                int view_cnt = Integer.parseInt(view_cnt_node.getNodeValue());

                NodeList good_cnt_list = item.getElementsByTagName("good_cnt");
                Element good_cnt_element = (Element) good_cnt_list.item(0);
                Node good_cnt_node = good_cnt_element.getFirstChild();
                int good_cnt = Integer.parseInt(good_cnt_node.getNodeValue());

                NodeList flag_list = item.getElementsByTagName("flag");
                Element flag_element = (Element) flag_list.item(0);
                Node flag_node = flag_element.getFirstChild();
                int flag = Integer.parseInt(flag_node.getNodeValue());

                AnimalSet as = new AnimalSet(idx, type, img_url, detail, view_cnt, good_cnt, flag);
                animalset_list.add(as);
                title_list.add(img_url);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return animalset_list;
    }
}
