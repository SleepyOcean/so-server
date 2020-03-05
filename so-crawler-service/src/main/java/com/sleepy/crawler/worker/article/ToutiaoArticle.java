package com.sleepy.crawler.worker.article;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.tools.DateTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.HttpTools;
import com.sleepy.crawler.dto.ArticleDTO;
import com.sleepy.crawler.dto.TransferDTO;
import com.sleepy.crawler.worker.CrawlerWork;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 今日头条
 *
 * @author gehoubao
 * @create 2020-03-05 18:03
 **/
@Slf4j
public class ToutiaoArticle implements CrawlerWork {
    @Override
    public List<TransferDTO> produce() throws IOException {
        List<TransferDTO> articles = new ArrayList<>();
        String html = FileTools.readToString("E:\\Code\\Web\\source\\all\\沉睡的海洋的头条主页 - 今日头条(www.toutiao.com).html");
        Document doc = Jsoup.parse(html);
        List<String> articleUrls = doc.getElementsByClass("lbox")
                .stream().map(o -> o.getElementsByTag("a").get(0).attr("href").replace("https://www.toutiao.com/item/", "https://www.toutiao.com/i")).collect(Collectors.toList());

        for (int i = 0; i < articleUrls.size(); i++) {
            Document articleDoc;
            try {
                articleDoc = HttpTools.getHtmlPageResponseAsDocument(articleUrls.get(i));
            } catch (Exception e) {
                log.error("访问头条失败： " + e.getMessage());
                continue;
            }
            String articleInfoHtml = articleDoc.body().getElementsByTag("script").get(3).html();
            JSONObject articleObject = JSON.parseObject("{"
                    + articleInfoHtml.substring(33, articleInfoHtml.length() - 12)
                    .replace(".slice(6, -6).replace(/<br \\/>/ig, '')", "")
                    .replace(".slice(6, -6)", "").replace("\\", "\\\\")
                    .replace("\\\\\"", "\\\"") + "}");

            System.out.println(articleUrls.get(i) + "   good");
            String content = StringEscapeUtils.unescapeEcmaScript(StringEscapeUtils.unescapeHtml4(articleInfoHtml.substring(articleInfoHtml.indexOf("content") + 10, articleInfoHtml.lastIndexOf(".slice(6, -6),") - 1))).replaceAll("&gt;", ">").replaceAll("&lt;", "<");
            String title = StringEscapeUtils.unescapeHtml4(articleObject.getJSONObject("articleInfo").getString("title")).replace("\\\\", "\\");
            String summary = StringEscapeUtils.unescapeHtml4(articleObject.getJSONObject("shareInfo").getString("abstract").replace("\\\\", "\\")).replaceAll("&gt;", ">").replaceAll("&lt;", "<");

            ArticleDTO entity = new ArticleDTO();
            entity.setCoverImg(articleObject.getJSONObject("articleInfo").getString("coverImg"));
            entity.setTitle(title.substring(1, title.length() - 1));
            entity.setCreateTime(DateTools.toDate(articleObject.getJSONObject("articleInfo").getJSONObject("subInfo").getString("time"), DateTools.DEFAULT_DATETIME_PATTERN));
            JSONArray tagsArray = articleObject.getJSONObject("articleInfo").getJSONObject("tagInfo").getJSONArray("tags");
            StringBuilder tagStr = new StringBuilder();
            for (int j = 0; j < tagsArray.size(); j++) {
                tagStr.append(tagsArray.getJSONObject(j).getString("name"));
                tagStr.append(",");
            }
            entity.setTags(tagStr.length() > 1 ? tagStr.substring(0, tagStr.length() - 1) : "");
            entity.setContent(content.substring(1, content.length() - 1));
            entity.setSummary(summary.substring(1, summary.length() - 1));
        }
        return articles;
    }
}