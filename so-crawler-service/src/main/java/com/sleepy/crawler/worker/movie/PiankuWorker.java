package com.sleepy.crawler.worker.movie;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.crawler.dto.MovieDTO;
import com.sleepy.crawler.dto.TransferDTO;
import com.sleepy.crawler.worker.CrawlerWork;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抓取片库网资源
 *
 * @author gehoubao
 * @create 2020-10-21 20:34
 **/
public class PiankuWorker implements CrawlerWork {

    @Override
    public List<TransferDTO> produce() throws IOException {
        List<TransferDTO> movies = new ArrayList<>();
        String sourceUrl = "https://www.pianku.tv";
        String baseUrlPrefix = sourceUrl + "/mv/----number--";

        long totalPage = getTotalPage("https://www.pianku.tv/mv/");

        for (int i = 0; i < 1; i++) {
            Document doc = Jsoup.connect(baseUrlPrefix + i + ".html").get();
            List<Element> pageContentList = doc.getElementsByClass("content-list").get(0).getElementsByTag("li");
            pageContentList.forEach(e -> {
                String itemDetailPageUrl = sourceUrl + e.getElementsByTag("a").get(1).attr("href");
                try {
                    MovieDTO data = getMovieDetail(itemDetailPageUrl);
                    movies.add(data);
                    System.out.println(String.format("index: %s, name: %s", movies.size(), data.getName()));
                    Thread.sleep(10000);
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
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
        String originalName = name.substring(name.indexOf(" ") + 1, name.lastIndexOf(" "));
        String chineseName = name.substring(0, name.indexOf(" "));
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
                    if (s.contains("豆瓣")) {
                        doubanStar = scoreSplit[2];
                        doubanLink = detail.getElementsByTag("a").get(0).attr("href");
                    } else if (s.contains("IMDB")) {
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
                actor = detailText.substring(3);
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
                .setPostUrlVertical(coverUrl)
                .setDownloadLinks(JSON.toJSONString(downloadList));

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
        return data;
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
                intros.add(intro.substring(lastIndex, i).trim());
                lastIndex = i + 1;
            }
        }
        intros.add(intro.substring(lastIndex, intro.length() - 1).trim());
        return JSON.toJSONString(intros);
    }

    private long getTotalPage(String pageUrl) throws IOException {
        Document firstPage = Jsoup.connect(pageUrl).get();
        List<Element> list = firstPage.getElementsByClass("pages").get(0).getAllElements();
        long page = Long.parseLong(list.get(list.size() - 2).text().substring(2));
        return page;
    }

    public static void main(String[] args) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
//        new PiankuWorker().getMovieDetail("https://www.pianku.tv/mv/wNiNWZxMmM.html");
        String data = JSON.toJSONString(new PiankuWorker().produce());
        FileTools.writeToString("G:\\movieTestData.json", data);
//        new PiankuWorker().trimIntroToJsonArrayString("　　2003年，伊拉克战争爆发。萨达姆政府受到重创，大规模杀伤性武器（WMD）成为美国出兵伊拉克的有力借口。罗伊·米勒（马特·戴蒙 Matt Damon 饰）所率领的小分队奉命在伊拉克境内寻找WMD，然而无数次的搜寻皆无所获，这令米勒对线报的来源心生疑惑。某次行动中，他遇到当地的独腿男子法哈迪，从对方口中米勒得知一众伊拉克关键人物正在某地集会，行动中意外发现扑克牌通缉令上的艾尔·拉威（Yigal Naor 饰）也在其中，而艾尔·拉威似乎和美国政府有着错综复杂的关系。米勒不顾劝阻展开独立调查，发现所谓的WMD不过是包藏了无数丑恶真相的谎言而已……\n" +
//                "　　本片根据《华盛顿邮报》驻巴格达记者拉吉夫·产德拉斯卡兰（Rajiv Chandrasekaran）的作品《翡翠城的帝王生活：伊拉克绿色地带深处》改编。");
    }
}