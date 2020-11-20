package com.sleepy.crawler.worker.movie;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.crawler.dto.TransferDTO;
import com.sleepy.crawler.worker.CrawlerWork;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 豆瓣电影数据爬取
 *
 * @author gehoubao
 * @create 2020-11-15 17:11
 **/
public class DoubanWorker implements CrawlerWork {
    @Value("${outPutPath:./dataset}")
    private String outputPath;

    @Override
    public List<TransferDTO> produce() throws IOException {
        return null;
    }

    public Map<String, Object> getMoviePicMap(String link) {
        List<String> videoCutList = new ArrayList<>();
        List<String> postList = new ArrayList<>();
        List<String> wallpaperList = new ArrayList<>();
        if (StringUtils.isEmpty(link)) return new HashMap<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(link + "/all_photos").get();
            List<Element> groupElements = doc.getElementsByClass("pic-col5");
            if (groupElements.size() > 0) {
                Thread.sleep(1000);
                doc = Jsoup.connect(link + "/photos?type=S&start=0&sortby=size&size=a&subtype=a").get();
                List<Element> photoElements = doc.getElementsByClass("poster-col3").get(0).getElementsByTag("img");
                for (Element element : photoElements) {
                    String src = element.attr("src");
                    String key = src.substring(src.lastIndexOf("/") + 1);
                    String originalUrl = "https://img3.doubanio.com/view/photo/r/public/" + key;
                    videoCutList.add(originalUrl);
                }
            }
            if (groupElements.size() > 1) {
                Thread.sleep(1000);
                doc = Jsoup.connect(link + "/photos?type=R&start=0&sortby=like&size=a&subtype=a").get();
                List<Element> photoElements = doc.getElementsByClass("poster-col3").get(0).getElementsByTag("img");
                for (Element element : photoElements) {
                    String src = element.attr("src");
                    String key = src.substring(src.lastIndexOf("/") + 1);
                    String originalUrl = "https://img3.doubanio.com/view/photo/r/public/" + key;
                    postList.add(originalUrl);
                }
            }
            if (groupElements.size() == 3) {
                Thread.sleep(1000);
                doc = Jsoup.connect(link + "/photos?type=W&start=0&sortby=size&size=a&subtype=a").get();
                List<Element> photoElements = doc.getElementsByClass("poster-col3").get(0).getElementsByTag("img");
                for (Element element : photoElements) {
                    String src = element.attr("src");
                    String key = src.substring(src.lastIndexOf("/") + 1);
                    String originalUrl = "https://img3.doubanio.com/view/photo/r/public/" + key;
                    wallpaperList.add(originalUrl);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("获取豆瓣图片出错：" + link + "/all_photos");
        }

        return CommonTools.getCustomMap(
                new MapModel("videoCutList", JSON.toJSONString(videoCutList)),
                new MapModel("postList", JSON.toJSONString(postList)),
                new MapModel("wallpaperList", JSON.toJSONString(wallpaperList)));
    }

    public List<String> getMovieTrailerLinkList(String link) {
        List<String> movieTrailerList = new ArrayList<>();
        if (StringUtils.isEmpty(link)) return new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(link + "/trailer#trailer").get();
            List<Element> videoList = doc.getElementsByClass("video-list");
            if (videoList.size() > 0) {
                List<Element> trailerElementList = doc.getElementsByClass("video-list").get(0).getElementsByClass("pr-video");
                for (int i = 0; i < (trailerElementList.size() > 3 ? 3 : trailerElementList.size()); i++) {
                    Element element = trailerElementList.get(i);
                    String trailerPage = element.getElementsByTag("a").get(0).attr("href");
                    Document trailerDoc = Jsoup.connect(trailerPage).get();
                    String trailerLink = trailerDoc.getElementsByTag("source").get(0).attr("src");
                    movieTrailerList.add(trailerLink);
                    Thread.sleep(2000);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("获取豆瓣预告片出错：" + link + "/trailer#trailer");
        }
        return movieTrailerList;
    }

    public static void main(String[] args) throws IOException {
//        new DoubanWorker().getMoviePicMap("https://movie.douban.com/subject/2973079");
//        System.out.println(linkList);
    }
}