package com.sleepy.crawler.worker.movie;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.ImageTools;
import com.sleepy.crawler.dto.MovieDTO;
import com.sleepy.crawler.dto.TransferDTO;
import com.sleepy.crawler.worker.CrawlerWork;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 抓取片库网资源
 *
 * @author gehoubao
 * @create 2020-10-21 20:34
 **/
@Component
@Slf4j
public class PiankuWorker implements CrawlerWork {
    private String sourceUrl = "https://www.pianku.tv";
    private String detailUrlPrefix = "https://www.pianku.tv/mv/";
    private List<String> downloadIds = new ArrayList<>();
    private List<String> errorIds = new ArrayList<>();
    private DoubanWorker doubanWorker = new DoubanWorker();
    private int failedContinuesTimes = 0;
    private boolean doubanRelationDownload = false;
    private int lastOutputDetialIndex = 1;
    @Value("${failedContinuesThreshold:10}")
    private int failedContinuesThreshold;
    @Value("${startIndex:1}")
    private int startIndex;
    @Value("${outPutPath:./dataset}")
    private String outputPath;

    @Override
    public List<TransferDTO> produce() throws IOException, InterruptedException {
        restoreDownloadData();

        log.info("=============获取所有页面详情地址-开始=============");
        List<String> allDetailUrl = new ArrayList<>();
        try {
            allDetailUrl = JSON.parseArray(FileTools.readToString(outputPath + File.separator + "all-detail-uuid-list.json"), String.class);
            if (null == allDetailUrl || allDetailUrl.isEmpty()) {
                allDetailUrl = getAllItemDetailPageUrl();
                FileTools.writeString(outputPath + File.separator + "all-detail-uuid-list.json", JSON.toJSONString(allDetailUrl));
            }
        } catch (Exception e) {
            log.info(String.format("获取所有页面详情地址-失败，原因：%s", e.getMessage()));
            return new ArrayList<>();
        }
        log.info("=============获取所有页面详情地址-完成=============");
        log.info(String.format("总数据条数： %s", allDetailUrl.size()));

        log.info("=============爬取『片库网』数据--开始=============");
        List<TransferDTO> movieSet = new ArrayList<>();
        boolean lastFailed = false;

        for (int i = startIndex; i < allDetailUrl.size(); i++) {
            // 连续失败超过指定阈值，则停止任务
            if (failedContinuesTimes > failedContinuesThreshold) {
                return movieSet;
            }
            String detailUUID = allDetailUrl.get(i);
            String itemDetailPageUrl = detailUrlPrefix + detailUUID + ".html";
            try {
                if (!downloadIds.contains(detailUUID)) {
                    MovieDTO data = getMovieDetail(itemDetailPageUrl);
                    log.info(String.format("index: %s, name: %s", i, data.getName()));
                    downloadIds.add(data.getUuid());
                    movieSet.add(data);
                    lastOutputDetialIndex = i;
                    Thread.sleep(2000);
                }
            } catch (Exception exception) {
                log.info(String.format("出错了：[%s]%s", itemDetailPageUrl, exception.getMessage()));
                errorIds.add(detailUUID);
                if (lastFailed) {
                    failedContinuesTimes++;
                } else {
                    failedContinuesTimes = 0;
                }
            }
        }

        log.info("=============爬取『片库网』数据--结束=============");
        return movieSet;
    }

    /**
     * 恢复之前已下载的记录及错误的记录
     *
     * @throws IOException
     */
    private void restoreDownloadData() {
        log.info("恢复历史下载记录--开始");
        String downloadedListJson = null;
        try {
            downloadedListJson = FileTools.readToString(outputPath + File.separator + "downloaded-id-list.json");
            downloadIds = JSON.parseArray(downloadedListJson, String.class);
        } catch (IOException e) {
            log.info("未发现已下载的历史记录！");
        }
        if (null == downloadIds) {
            downloadIds = new ArrayList<>();
        }

        String errorListJson = null;
        try {
            errorListJson = FileTools.readToString(outputPath + File.separator + "error-id-list.json");
            List<String> errorIdsRecord = JSON.parseArray(errorListJson, String.class);
            if (null != errorIdsRecord && !errorIdsRecord.isEmpty()) {
                // 恢复上次失败的下载
                errorIdsRecord.forEach(uuid -> {
                    try {
                        if (!downloadIds.contains(uuid)) {
                            String itemDetailPageUrl = detailUrlPrefix + uuid + ".html";

                            MovieDTO data = getMovieDetail(itemDetailPageUrl);
                            log.info(String.format("恢复下载成功，name: %s", data.getName()));
                            downloadIds.add(data.getUuid());
                            Thread.sleep(2000);
                        }
                    } catch (Exception e) {
                        log.info(String.format("恢复下载出错了：[%s]%s", uuid, e.getMessage()));
                        errorIds.add(uuid);
                    }
                });
            }
        } catch (IOException e) {
            log.info("未发现下载失败的历史记录！");
        }

        try {
            lastOutputDetialIndex = Integer.parseInt(FileTools.readToString(outputPath + File.separator + "last-detail-index.json").trim());
            if (startIndex == 1) {
                startIndex = lastOutputDetialIndex;
            }
        } catch (IOException | NumberFormatException e) {
            log.info("未发现上次下载索引！");
        }

        log.info("恢复历史下载记录--结束");
    }

    /**
     * 获取所有页的电影详情id
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<String> getAllItemDetailPageUrl() throws IOException, InterruptedException {
        List<String> urlList = new ArrayList<>();

        String baseUrlPrefix = sourceUrl + "/mv/----number--";
        long totalPage = getTotalPage(baseUrlPrefix + "1.html");
        for (int i = 1; i < totalPage; i++) {
            String currentPageUrl = baseUrlPrefix + i + ".html";
            Document doc = null;
            try {
                doc = Jsoup.connect(currentPageUrl).get();
            } catch (Exception e) {
                log.info(String.format("%s页已出错了, %s", i, e.getMessage()));
                continue;
            }
            List<Element> pageContentList = doc.getElementsByClass("content-list").get(0).getElementsByTag("li");
            List<String> ids = new ArrayList<>();
            pageContentList.forEach(e -> {
                String itemDetailPageUrl = sourceUrl + e.getElementsByTag("a").get(1).attr("href");
                String uuid = itemDetailPageUrl.substring(itemDetailPageUrl.lastIndexOf("/") + 1, itemDetailPageUrl.lastIndexOf(".html"));
                ids.add(uuid);
            });
            urlList.addAll(ids);
            log.info(String.format("%s页已添加完毕, %s", i, JSON.toJSON(ids)));
            Thread.sleep(2000);
        }
        return urlList;
    }

    /**
     * 获取电影详情信息，并将电影详情信息写入对应的目录的index.json文件中
     *
     * @param itemDetailPageUrl 电影详情页的网址
     * @return
     * @throws IOException
     */
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
                + String.format(" - (%s) %s", data.getChineseName(), data.getOriginalName());

        if (doubanRelationDownload && !StringUtils.isEmpty(doubanLink)) {
            Map<String, Object> doubanPicLinkMap = doubanWorker.getMoviePicMap(doubanLink);
            data.setPostUrlVertical(doubanPicLinkMap.get("postList").toString())
                    .setPostUrlHorizon(doubanPicLinkMap.get("wallpaperList").toString())
                    .setCaptureUrls(doubanPicLinkMap.get("videoCutList").toString());
            data.setTrailerUrls(JSON.toJSONString(doubanWorker.getMovieTrailerLinkList(doubanLink)));

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
        String old = FileTools.readToString(currentMoviePath + File.separator + "index.json");
        if (StringUtils.isEmpty(old) || !data.equals(JSON.parseObject(old, MovieDTO.class))) {
            FileTools.writeString(currentMoviePath + File.separator + "index.json", JSON.toJSONString(data));
        }
        return data;
    }

    /**
     * 输出任务所有记录
     */
    public void printLastLog() {
        FileTools.writeString(outputPath + File.separator + "error-id-list.json", JSON.toJSONString(new HashSet<>(errorIds)));
        FileTools.writeString(outputPath + File.separator + "downloaded-id-list.json", JSON.toJSONString(new HashSet<>(downloadIds)));
        FileTools.writeString(outputPath + File.separator + "last-detail-index.json", lastOutputDetialIndex + "");
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