package com.sleepy.crawler.worker.custom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.tools.ExcelTools;
import com.sleepy.common.tools.FileTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;

/**
 * 商品数据
 *
 * @author gehoubao
 * @create 2020-03-07 11:41
 **/
public class GoodsInfo {

    public static void main(String[] args) throws Exception {
        new GoodsInfo().getGoodsListFromExcel("E:\\MicroProjects\\assets\\商品导入模板V2.0.xlsx");
    }

    private void getContentFromTianMall() throws Exception {
        String html = FileTools.readToString("E:\\Code\\\\resuource\\天猫美食-喵鲜生-理想生活上天猫.html");
        Document doc = Jsoup.parse(html);
        Map<String, Object> res = new HashMap<>();
        List<Object> array = new ArrayList<>();
        doc.getElementsByClass("itemBox").forEach(box -> {
            String imgAlt = box.getElementsByTag("img").get(0).attr("alt");
            String imgUrl = box.getElementsByTag("img").get(0).attr("src").replace("./天猫美食-喵鲜生-理想生活上天猫_files/", "//img.alicdn.com/bao/uploaded/i1/");
            String goodsName = box.getElementsByClass("desc").text();
            String oldPrice = box.getElementsByClass("oprice").get(0).getElementsByTag("span").get(1).text();
            String nowPrice = box.getElementsByClass("price").get(0).getElementsByTag("span").get(1).text();
            Map<String, Object> item = new HashMap<>(8);
            item.put("goodsName", "".equalsIgnoreCase(imgAlt) ? goodsName : imgAlt);
            item.put("goodsPriceOrigin", oldPrice);
            item.put("goodsPriceNow", nowPrice);
            item.put("imgUrl", imgUrl);
            item.put("detailImgUrl", imgUrl);
            item.put("storageNum", (new Random()).nextInt(1000));
            item.put("goodsDesc", "".equalsIgnoreCase(imgAlt) ? goodsName : imgAlt + "  " + goodsName);
            item.put("category", (new Random()).nextInt(7) + 1);
            int subLength = 1;
            switch (Integer.parseInt(item.get("category").toString())) {
                case 1:
                    subLength = 16;
                    break;
                case 2:
                    subLength = 8;
                    break;
                case 3:
                    subLength = 12;
                    break;
                case 4:
                    subLength = 4;
                    break;
                case 5:
                    subLength = 3;
                    break;
                case 6:
                    subLength = 6;
                    break;
                case 7:
                    subLength = 4;
                    break;
            }
            item.put("subCategory", (new Random()).nextInt(subLength) + 1);
            array.add(item);
        });
        res.put("goods", array);
        System.out.println(JSON.toJSON(res).toString());
    }

    private void getGoodsListFromExcel(String path) {
        Map<String, List<List<String>>> result = ExcelTools.readExcelByString("E:\\MicroProjects\\assets\\商品导入模板V1.1(1)(1).xls");
        List<Object> list = (List<Object>) result.values().toArray()[0];
        Map<String, Object> res = new HashMap<>(4);
        List<Object> array = new ArrayList<>();
        for (int i = 5; i < list.size() - 1; i++) {
            List<String> originGoods = (List<String>) list.get(i);
            Map<String, Object> item = new HashMap<>(8);
//            item.put("goodsName", "".equalsIgnoreCase(imgAlt) ? goodsName : imgAlt);
//            item.put("goodsPriceOrigin", oldPrice);
//            item.put("goodsPriceNow", nowPrice);
//            item.put("imgUrl", imgUrl);
//            item.put("detailImgUrl", imgUrl);
//            item.put("storageNum", (new Random()).nextInt(1000));
//            item.put("goodsDesc", "".equalsIgnoreCase(imgAlt) ? goodsName : imgAlt + "  " + goodsName);
//            item.put("category", (new Random()).nextInt(7) + 1);
            array.add(item);
        }

        res.put("goods", array);
        System.out.println(JSON.toJSON(res).toString());
    }

    private void writeGoodsListToExcel(String path) {
        Map<String, List<List<String>>> result = ExcelTools.readExcelByString("E:\\MicroProjects\\assets\\商品导入模板V2.0.xlsx");
        List<JSONObject> data = new ArrayList<>();
        List<List<String>> list = result.get("模板");
        data.forEach(d -> {
            List<String> item = new ArrayList<>();
            item.set(3, d.get("商品名称").toString());
            list.add(item);
        });
        ExcelTools.writeExcelByString("E:\\MicroProjects\\assets\\商品导入模板V3.0.xlsx", result);
    }
}