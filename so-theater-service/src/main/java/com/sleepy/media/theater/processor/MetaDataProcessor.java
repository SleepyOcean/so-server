package com.sleepy.media.theater.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXWriter;

/**
 * 影视元数据处理类
 *
 * @author gehoubao
 * @create 2021-06-20 17:02
 **/
public class MetaDataProcessor {

    public void generateMetaDataFileByTMDbJson(String tmdbJsonStr) {
        JSONObject jsonObject = JSON.parseObject(tmdbJsonStr);
        SAXWriter writer = new SAXWriter();
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("movie");

        Element author1 = root.addElement("author")
                .addAttribute("name", "James")
                .addAttribute("location", "UK")
                .addText("James Strachan");

        Element author2 = root.addElement("author")
                .addAttribute("name", "Bob")
                .addAttribute("location", "US")
                .addText("Bob McWhirter");

    }
}