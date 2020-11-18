package com.sleepy.crawler.worker.movie;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.ImageTools;
import com.sleepy.crawler.dto.MovieDTO;
import com.sleepy.crawler.dto.TransferDTO;
import com.sleepy.crawler.worker.CrawlerWork;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 抓取片库网资源
 *
 * @author gehoubao
 * @create 2020-10-21 20:34
 **/
@Component
public class PiankuWorker implements CrawlerWork {
    private String sourceUrl = "https://www.pianku.tv";
    private Set<String> downloadIds = new HashSet<>();
    private List<String> errorUrl = new ArrayList<>();
    private DoubanWorker doubanWorker = new DoubanWorker();
    private int lastOutputPageIndex = 1;
    @Value("${startPage:1}")
    private int startPage;
    @Value("${outPutPath:./dataset}")
    private String outputPath;

    public static void main(String[] args) {
        System.out.println();
    }

    @Override
    public List<TransferDTO> produce() throws IOException {
        System.out.println("爬取『片库网』数据--开始");
        List<TransferDTO> movieSet = new ArrayList<>();
        String baseUrlPrefix = sourceUrl + "/mv/----number--";
        boolean sumPrintFlag = true;
        long totalPage = getTotalPage(baseUrlPrefix + "1.html");
        for (int i = startPage; i < 2; i++) {
            lastOutputPageIndex = i;
            String currentPageUrl = baseUrlPrefix + i + ".html";
            Document doc = Jsoup.connect(currentPageUrl).get();
            List<Element> pageContentList = doc.getElementsByClass("content-list").get(0).getElementsByTag("li");
            if (i == 1 && sumPrintFlag) {
                System.out.println(String.format("总页数：%s， 每页条数：%s， 预估总数据条数： %s", totalPage, pageContentList.size(), totalPage * pageContentList.size()));
                sumPrintFlag = false;
            }
            System.out.println(String.format("当前数据网址：%s", currentPageUrl));
            List<TransferDTO> currentPageData = getCurrentPageInfo(pageContentList.subList(20, 25));
            movieSet.addAll(currentPageData);

            String data = JSON.toJSONString(currentPageData);
            System.out.println(String.format("当前数据网址：%s, 数据写入完成~ 数据输出：", currentPageUrl, data));
        }
        System.out.println("爬取『片库网』数据--结束");
        return movieSet;
    }

    private List<TransferDTO> getCurrentPageInfo(List<Element> pageContentList) {
        List<TransferDTO> movies = new ArrayList<>();
        for (Element e : pageContentList) {
            String itemDetailPageUrl = sourceUrl + e.getElementsByTag("a").get(1).attr("href");
            String uuid = itemDetailPageUrl.substring(itemDetailPageUrl.lastIndexOf("/") + 1, itemDetailPageUrl.lastIndexOf(".html"));
            try {
                if (!downloadIds.contains(uuid)) {
                    MovieDTO data = getMovieDetail(itemDetailPageUrl);
                    movies.add(data);
                    System.out.println(String.format("index: %s, name: %s", movies.size(), data.getName()));
                    downloadIds.add(data.getUuid());
                    Thread.sleep(10000);
                }
            } catch (Exception exception) {
                System.out.println(String.format("出错了：[%s]%s", itemDetailPageUrl, exception.getMessage()));
                errorUrl.add(itemDetailPageUrl);
            }
        }
        FileTools.writeToString(outputPath + File.separator + "movie-all-dataset.json", JSON.toJSONString(movies));
        return movies;
    }

    private MovieDTO getMovieDetail(String itemDetailPageUrl) throws IOException {
        String downloadLinkUrl = itemDetailPageUrl.replace("mv", "ajax/downurl").replace(".html", "_mv/");
        Document detailPage = Jsoup.connect(itemDetailPageUrl).get();
        Document downloadPage = Jsoup.connect(downloadLinkUrl).cookies(Jsoup.connect(itemDetailPageUrl).execute().cookies()).get();
        String keyNodeStr = downloadPage.getElementsByTag("script").get(0).childNodes().get(0).toString();
        String key = keyNodeStr.substring(keyNodeStr.indexOf("decontent('") + 11, keyNodeStr.indexOf("decontent('") + 11 + 13);

        String coverUrl = detailPage.getElementsByClass("cover").get(0).getElementsByTag("img").get(0).attr("src");
        String name = detailPage.getElementsByClass("main-ui-meta").get(0).getElementsByTag("h1").text();
        String chineseName = name.substring(0, name.indexOf(" "));
        String originalName = chineseName;
        if (name.split(" ").length > 2) {
            originalName = name.substring(name.indexOf(" ") + 1, name.lastIndexOf(" "));
        }
        String publishYear = name.substring(name.lastIndexOf(" "), name.length()).substring(2, 6);
        List<Element> details = detailPage.getElementsByClass("main-ui-meta").get(0).getElementsByTag("div");
        String uuid = itemDetailPageUrl.substring(itemDetailPageUrl.lastIndexOf("/") + 1, itemDetailPageUrl.lastIndexOf(".html"));
        String note = "";
        String director = "";
        String scriptwriter = "";
        String actor = "";
        String movieType = "";
        String country = "";
        String date = "";
        String runningTime = "";
        String updateTime = "";
        String alias = "";
        String score = "";
        String doubanLink = "";
        String doubanStar = "";
        String starIMDB = "";
        for (int i = 1; i < details.size(); i++) {
            Element detail = details.get(i);
            String detailText = detail.text();
            if (detailText.contains("评分：")) {
                score = detailText.substring(4);
                StringBuilder scoreFormat = new StringBuilder();
                String[] scoreList = score.split(" / ");
                for (String s : scoreList) {
                    String[] scoreSplit = s.split(" ");
                    scoreFormat.append(scoreSplit[0] + " " + scoreSplit[1]);
                    if (scoreSplit.length > 2) {
                        scoreFormat.append(" " + scoreSplit[scoreSplit.length - 1]);
                    }
                    scoreFormat.append(" / ");
                    if (s.contains("豆瓣") && !s.contains("N/A")) {
                        doubanStar = scoreSplit[2];
                        doubanLink = detail.getElementsByTag("a").get(0).attr("href");
                    } else if (s.contains("IMDB") && !s.contains("N/A")) {
                        starIMDB = scoreSplit[2];
                    }
                }
                scoreFormat.substring(0, scoreFormat.lastIndexOf(" / "));
            } else if (detailText.contains("又名："))
                alias = detailText.substring(3);
            else if (detailText.contains("片长："))
                runningTime = detailText.substring(3);
            else if (detailText.contains("上映："))
                date = detailText.substring(3);
            else if (detailText.contains("地区："))
                country = detailText.substring(3);
            else if (detailText.contains("类型："))
                movieType = detailText.substring(3);
            else if (detailText.contains("主演："))
                actor = detailText.substring(3).replace("展开...", "").replace("...收起", "");
            else if (detailText.contains("导演："))
                director = detailText.substring(3);
            else if (detailText.contains("编剧："))
                scriptwriter = detailText.substring(3);
            else if (detailText.contains("当前")) {
                note = detailText;
                if (note.length() > 10) {
                    updateTime = note.substring(note.length() - 10);
                }
            }
        }
        String intro = detailPage.getElementsByClass("movie-introduce").get(0).text();
        if (intro.contains("[展开全部]")) {
            intro = intro.substring(intro.indexOf("[展开全部]") + 6 + 3);
        }
        if (intro.contains("[收起部分]")) {
            intro = intro.substring(0, intro.lastIndexOf("[收起部分]"));
        }
        intro = trimIntroToJsonArrayString(intro);
        List<Element> downloadListElements = downloadPage.getElementsByClass("down-list").get(0).getElementsByTag("li");
        List<Map> downloadList = new ArrayList<>();
        downloadListElements.forEach(element -> {
            String encryptLink = element.getElementsByTag("a").get(0).attr("href").substring(1);
            String linkName = element.getElementsByTag("a").get(0).attr("title");
            String ratio = "";
            String fileSize = "";
            if (linkName.contains("[") && linkName.contains("]") && (linkName.contains("GB") || linkName.contains("MB"))) {
                fileSize = linkName.substring(linkName.lastIndexOf('[') + 1, linkName.lastIndexOf(']'));
            }
            if (linkName.contains("480P") || linkName.contains("480p")) {
                ratio = "480P";
            }
            if (linkName.contains("720P") || linkName.contains("720p")) {
                ratio = "720P";
            }
            if (linkName.contains("1080P") || linkName.contains("1080p")) {
                ratio = "1080P";
            }
            if (linkName.contains("2160P") || linkName.contains("2160p")) {
                ratio = "2160P";
            }


            downloadList.add(CommonTools.getCustomMap(
                    new MapModel("linkName", linkName),
                    new MapModel("downloadUrl", DecrptTools.tripleDesDecrypt(encryptLink, key)),
                    new MapModel("fileSize", fileSize),
                    new MapModel("ratio", ratio)));
        });
        MovieDTO data = MovieDTO.newBuilder()
                .setUuid(uuid)
                .setName(name)
                .setChineseName(chineseName)
                .setOriginalName(originalName)
                .setPublishYear(publishYear)
                .setDirector(director)
                .setScriptwriter(scriptwriter)
                .setActor(actor)
                .setCountry(country)
                .setAlias(alias)
                .setDate(date)
                .setUpdateTime(updateTime)
                .setRunningTime(runningTime)
                .setIntro(intro)
                .setScore(score)
                .setNote(note)
                .setType(movieType)
                .setLinkDouban(doubanLink)
                .setPostUrl(coverUrl)
                .setDownloadLinks(JSON.toJSONString(downloadList));

        String currentMoviePath = outputPath + File.separator + data.getPublishYear()
                + String.format(" - (%s) %s.%s", data.getChineseName(), data.getOriginalName(), data.getPublishYear());
        if (!StringUtils.isEmpty(doubanLink)) {
            Map<String, Object> doubanPicLinkMap = doubanWorker.getMoviePicMap(doubanLink);
            data.setPostUrlVertical(doubanPicLinkMap.get("postList").toString())
                    .setPostUrlHorizon(doubanPicLinkMap.get("wallpaperList").toString())
                    .setCaptureUrls(doubanPicLinkMap.get("videoCutList").toString());
            data.setTrailerUrls(JSON.toJSONString(doubanWorker.getMovieTrailerLinkList(doubanLink)));

//            for (String imgUrl : JSON.parseArray(data.getCaptureUrls(), String.class)) {
//                ImageTools.downloadImg(imgUrl, currentMoviePath + File.separator + "capture", imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.lastIndexOf(".")));
//            }
            int size = 0;
            for (String imgUrl : JSON.parseArray(data.getPostUrlVertical(), String.class)) {
                ImageTools.downloadImg(imgUrl, currentMoviePath + File.separator + "post", imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.lastIndexOf(".")));
                if (size > 3) {
                    size = 0;
                    break;
                } else {
                    size++;
                    continue;
                }
            }
            for (String imgUrl : JSON.parseArray(data.getPostUrlHorizon(), String.class)) {
                ImageTools.downloadImg(imgUrl, currentMoviePath + File.separator + "wallpaper", imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.lastIndexOf(".")));
                if (size > 3) {
                    size = 0;
                    break;
                } else {
                    size++;
                    continue;
                }
            }
        }

        if (StringUtils.isEmpty(doubanStar)) {
            data.setScoreDouban("N/A");
        } else {
            data.setScoreDouban(doubanStar);
        }
        if (StringUtils.isEmpty(starIMDB)) {
            data.setScoreIMDB("N/A");
        } else {
            data.setScoreIMDB(starIMDB);
        }
        FileTools.writeToString(currentMoviePath + File.separator + "index.json", JSON.toJSONString(data));
        return data;
    }

    public void printLastLog() {
        FileTools.writeToString(outputPath + File.separator + "error-url-list.json", JSON.toJSONString(errorUrl));
        FileTools.writeToString(outputPath + File.separator + "downloaded-id-list.json", JSON.toJSONString(downloadIds));
        FileTools.writeToString(outputPath + File.separator + "last-page-index.json", lastOutputPageIndex + "");
    }

    private String trimIntroToJsonArrayString(String intro) {
        intro = intro.replace((char) 12288, ' ');

        intro = intro.trim();
        List<String> intros = new ArrayList<>();
        int lastIndex = 0;
        for (int i = 0; i < intro.length() - 1; i++) {
            char a = intro.charAt(i);
            char b = intro.charAt(i + 1);
            if (a == ' ' && b == ' ') {
                String introStr = intro.substring(lastIndex, i).trim();
                if (!StringUtils.isEmpty(introStr)) {
                    intros.add(introStr);
                }
                lastIndex = i + 1;
            }
        }
        String introStr = intro.substring(lastIndex, intro.length() - 1).trim();
        if (!StringUtils.isEmpty(introStr)) {
            intros.add(introStr);
        }
        return JSON.toJSONString(intros);
    }

    private long getTotalPage(String pageUrl) throws IOException {
        Document firstPage = Jsoup.connect(pageUrl).get();
        List<Element> list = firstPage.getElementsByClass("pages").get(0).getAllElements();
        long page = Long.parseLong(list.get(list.size() - 2).text().substring(2));
        return page;
    }
}