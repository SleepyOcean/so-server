package com.sleepy.crawler.worker.article;

import com.sleepy.common.tools.DateTools;
import com.sleepy.crawler.dto.ArticleDTO;
import com.sleepy.crawler.dto.TransferDTO;
import com.sleepy.crawler.worker.CrawlerWork;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 美团技术
 *
 * @author gehoubao
 * @create 2020-03-05 17:41
 **/
public class MeituanArticle implements CrawlerWork {
    @Override
    public List<TransferDTO> produce() throws IOException {
        List<TransferDTO> articles = new ArrayList<>();
        String url = "https://tech.meituan.com/page/";
        for (int j = 1; j < 18; j++) {
            Document doc = Jsoup.connect(url + j + ".html").get();
            List<String> articleUrls = doc.getElementsByClass("post-title")
                    .stream().map(o -> o.getElementsByTag("a").get(0).attr("href")).collect(Collectors.toList());
            System.out.println("loading meituan article");
            for (int i = 0; i < articleUrls.size(); i++) {
                Document articleDoc = Jsoup.connect(articleUrls.get(i)).get();
                System.out.println((i + 1) + ":\t" + articleUrls.get(i));
                ArticleDTO entity = new ArticleDTO();
                entity.setTitle(articleDoc.getElementsByClass("post-title").get(0).getElementsByTag("a").html());
                String date = Jsoup.parse(articleDoc.getElementsByClass("m-post-date").get(0).html()).text();
                String[] dateStrs = date.split("[\\u4e00-\\u9fa5]");
                StringBuilder createTime = new StringBuilder();
                createTime.append(dateStrs[0]);
                createTime.append("-");
                createTime.append(dateStrs[1]);
                createTime.append("-");
                createTime.append(dateStrs[2]);
                createTime.append(" 00:00:00");
                entity.setCreateTime(DateTools.toDate(createTime.toString(), DateTools.DEFAULT_DATETIME_PATTERN));
                String[] tags = articleDoc.getElementsByClass("tag-links").stream().map(o -> o.getElementsByTag("a").html()).collect(Collectors.toList()).get(0).split("\n");
                StringBuilder tagStr = new StringBuilder();
                for (String o : tags) {
                    tagStr.append(o);
                    tagStr.append(",");
                }
                entity.setTags(tagStr.substring(0, tagStr.length() - 1));
                entity.setContent(articleDoc.getElementsByClass("content").html());
                entity.setSummary(Jsoup.parse(articleDoc.getElementsByClass("content").get(0).getElementsByTag("p").get(0).html()).text());
                articles.add(entity);
            }
        }
        return articles;
    }
}