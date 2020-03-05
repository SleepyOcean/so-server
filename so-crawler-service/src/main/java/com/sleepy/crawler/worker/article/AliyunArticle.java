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
 * 阿里云栖
 *
 * @author gehoubao
 * @create 2020-03-05 18:00
 **/
public class AliyunArticle implements CrawlerWork {
    @Override
    public List<TransferDTO> produce() throws IOException {
        List<TransferDTO> articles = new ArrayList<>();
        String url = "https://yq.aliyun.com";
        Document doc = Jsoup.connect(url + "/articles").get();
        List<String> articleUrls = doc.getElementsByClass("item-box")
                .stream().map(o -> o.getElementsByTag("a").get(0).attr("href")).collect(Collectors.toList());

        for (int i = 0; i < articleUrls.size(); i++) {
            Document articleDoc = Jsoup.connect(url + articleUrls.get(i) + "?type=2").get();
            System.out.println("good");
            System.out.println(articleUrls.get(i));
            ArticleDTO entity = new ArticleDTO();
            entity.setTitle(articleDoc.getElementsByClass("blog-title").html());
            entity.setCreateTime(DateTools.toDate(articleDoc.getElementsByClass("b-time").html(), DateTools.DEFAULT_DATETIME_PATTERN));
            List<String> tags = articleDoc.getElementsByClass("label-item").stream().map(o -> o.getElementsByTag("span").html()).collect(Collectors.toList());
            StringBuilder tagStr = new StringBuilder();
            tags.forEach(o -> {
                tagStr.append(o);
                tagStr.append(",");
            });
            entity.setTags(tagStr.substring(0, tagStr.length() - 1));
            entity.setContent(articleDoc.getElementsByClass("markdown-body").html());
            entity.setSummary(Jsoup.parse(Jsoup.connect("https://yq.aliyun.com/articles/708486?type=2").get().getElementsByClass("markdown-body").get(0).getElementsByTag("p").get(0).html()).text());
            articles.add(entity);
        }
        return articles;
    }
}