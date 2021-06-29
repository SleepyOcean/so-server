package com.sleepy.common.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sleepy.common.model.MapModel;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Http请求工具类
 *
 * @author Captain
 * @create 2019-07-28 10:52
 */
public class HttpTools {

    public static final Integer HTTP_STATUS_OK = 200;
    public static final Integer HTTP_INTERNAL_SERVER_ERROR = 500;

    /**
     * 请求超时时间,默认1000ms
     */
    private static int TIME_OUT = 5000;
    /**
     * 等待异步JS执行时间,默认1000ms
     */
    private static int WAIT_SECOND = 5000;
    private static String UNKNOWN_IP = "unknown";


    /**
     * 模拟get请求接口返回json数据格式
     *
     * @param url
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) throws URISyntaxException, IOException {
        String result = "";
        //获取httpclient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder uriBuilder = new URIBuilder(url);
        params.forEach((key, value) -> {
            uriBuilder.addParameter(key, value.toString());
        });
        //获取get请求对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        try {
            //发起请求
            HttpResponse response = httpClient.execute(httpGet);
            //获取响应中的数据
            HttpEntity entity = response.getEntity();
            //把entity转换成字符串
            result = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
        }
        return result;
    }

    /**
     * 模拟get请求接口返回json数据格式
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        String result = "";
        //获取httpclient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        //获取get请求对象
        HttpGet httpGet = new HttpGet(url);
        try {
            //发起请求
            HttpResponse response = httpClient.execute(httpGet);
            //获取响应中的数据
            HttpEntity entity = response.getEntity();
            //把entity转换成字符串
            result = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
        }
        return result;
    }

    /**
     * post请求
     * <p>
     * /**
     * ①：创建一个HttpClient对象 client
     * ②：创建一个HttpGet对象
     * ③：使用client发起一个get请求
     * ④：获取HttpEntity响应
     * ⑤：解析这个响应对象
     *
     * @throws Exception
     */
    public static String doPost(String url, String param) {
        String result = "";
        //获取httpclient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        //获取请求对象
        HttpPost httpPost = new HttpPost(url);
        try {
            //把传入进来的结构树封装
            httpPost.setEntity(new StringEntity(param, "utf-8"));
            //执行一个post请求
            HttpResponse response = httpClient.execute(httpPost);
            //从响应获取数据
            HttpEntity entity = response.getEntity();
            //将httpEntity转换为string
            result = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
        }
        return result;
    }

    /**
     * 将网页返回为解析后的文档格式
     *
     * @param html
     * @return
     * @throws Exception
     */
    public static Document parseHtmlToDoc(String html) throws Exception {
        return removeHtmlSpace(html);
    }

    private static Document removeHtmlSpace(String str) {
        return Jsoup.parse(str);
    }

    /**
     * 获取页面文档字串(等待异步JS执行)
     *
     * @param url 页面URL
     * @return
     * @throws Exception
     */
    public static String getHtmlPageResponse(String url) throws Exception {
        String result = "";

        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //当JS执行出错的时候是否抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        //当HTTP的状态非200时是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setActiveXNative(true);
        //是否启用CSS
        webClient.getOptions().setCssEnabled(true);
        //很重要，启用JS
        webClient.getOptions().setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //设置“浏览器”的请求超时时间
        webClient.getOptions().setTimeout(TIME_OUT);
        //设置JS执行的超时时间
        webClient.setJavaScriptTimeout(TIME_OUT);

        HtmlPage page;
        try {
            page = webClient.getPage(url);
        } catch (Exception e) {
            webClient.close();
            throw e;
        }
        //该方法阻塞线程
        webClient.waitForBackgroundJavaScript(WAIT_SECOND);

        result = page.asXml();
        webClient.close();

        return result;
    }

    /**
     * 获取页面文档Document对象(等待异步JS执行)
     *
     * @param url 页面URL
     * @return
     * @throws Exception
     */
    public static Document getHtmlPageResponseAsDocument(String url) throws Exception {
        return parseHtmlToDoc(getHtmlPageResponse(url));
    }

    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!(UNKNOWN_IP.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }


    public static String sendPost(String url, String jsonParams) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;

        JSONObject params = JSON.parseObject(jsonParams);
        //解决中文乱码问题
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        System.out.println();

        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == HTTP_STATUS_OK) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
        }
        return respContent;
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("org.apache.commons.logging.LogFactory", "org.apache.commons.logging.impl.LogFactoryImpl");
//        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
//        LogFactory.getFactory().setAttribute("org.apache.commons.logging.simplelog.defaultlog", "error");
//        webAutoTest();
//        requestTest();
        offsetTest();
    }

    private static void requestTest() throws IOException {
        String devices = FileTools.readToString("E:\\Haiyan\\相关文档及资源\\海燕\\视图库\\deivceList.txt");
        String[] deviceArray = devices.split(",");

        Random random = new Random();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @SneakyThrows
            public void run() {
                String parmas = FileTools.readToString("E:\\Haiyan\\相关文档及资源\\海燕\\视图库\\过车数据-苏B9ZW50.json");
                String passTime = DateTools.dateFormat(new Date(), DateTools.CUSTOM_DATETIME_PATTERN);
                String deviceId = deviceArray[random.nextInt(deviceArray.length)];
                parmas = parmas.replaceAll("\"PassTime\":\"20200514131648\",", "\"PassTime\":\"" + passTime + "\",");
                parmas = parmas.replaceAll("\"AppearTime\":\"20200514131648\",", "\"AppearTime\":\"" + passTime + "\",");
                parmas = parmas.replaceAll("\"DisAppearTime\":\"20200514131648\",", "\"DisAppearTime\":\"" + passTime + "\",");
                parmas = parmas.replaceAll("\"MarkTime\":\"20200514131648\",", "\"MarkTime\":\"" + passTime + "\",");
                parmas = parmas.replaceAll("\"DeviceID\":\"32050500011120000858\",", "\"DeviceID\":\"" + deviceId + "\",");
                parmas = parmas.replaceAll("\"MotorVehicleID\":\"ed2e7f65c982481b947eebbbacf93d0c\",", "\"MotorVehicleID\":\"" + StringTools.getRandomUuid("").substring(0, 18) + passTime + "\",");
                String result = doPost("http://10.65.5.70:8000/VIID/Images", parmas);
                System.out.println(result);
            }
        }, 1000, 60000);
    }

    private static void offsetTest() {
        String sql = "SELECT A.PlateNo AS plateNo, A.PlateColor AS plateColor, A.FirstEnter AS passTime FROM (SELECT PlateID, PlateNo, PlateColor, MIN(PassTime) AS FirstEnter, COUNT(*) AS CNT FROM kdmotovehicle WHERE 1=1  AND PassTime >= '2020-04-15 16:32:58'   AND PassTime <= '2020-05-15 16:32:59'   and PassTimeHour >= 1586937600   and PassTimeHour <= 1589529600     AND PlateNo != '********' AND PlateNo != '00000000' GROUP BY PlateID HAVING CNT >= 1) A LEFT JOIN (SELECT DISTINCT PlateID FROM kdmotovehicle WHERE 1=1  AND PassTime >= '2020-01-16 16:32:58'   AND PassTimeHour >= 1579161600   AND PassTime <= '2020-04-15 16:32:58'   and PassTimeHour <= 1586937600    ) B ON A.PlateID = B.PlateID WHERE B.PlateID IS NULL";
        List<MapModel> offsetLimitList = new ArrayList<>();
        offsetLimitList.add(new MapModel("1000", 1000));
        offsetLimitList.add(new MapModel("1000", 2000));
        offsetLimitList.add(new MapModel("1000", 3000));
        offsetLimitList.add(new MapModel("2000", 1000));
        offsetLimitList.add(new MapModel("2000", 2000));
        offsetLimitList.add(new MapModel("2000", 3000));
        offsetLimitList.add(new MapModel("5000", 1000));
        offsetLimitList.add(new MapModel("5000", 2000));
        offsetLimitList.add(new MapModel("5000", 3000));

        offsetLimitList.forEach(p -> {
            String offsetLimit = " LIMIT " + p.getKey() + " OFFSET " + p.getValue();
            String params = sql + offsetLimit;
            double takeTime = 0D;
            double[] takeTimeArray = new double[3];
            for (int i = 0; i < 3; i++) {
                String s = HttpTools.doPost("http://localhost:8092/haiyan-server/resource/test/customSQL", params);
                takeTimeArray[i] = JSON.parseObject(s).getDouble("timeout");
                takeTime += takeTimeArray[i];
            }
            System.out.println(p.toString() + " : " + takeTimeArray[0] + " + " + takeTimeArray[1] + " + " + takeTimeArray[2] + " / 3 = " + StringTools.getValFormat(String.valueOf((takeTime / 3)), 4) + " s");
        });

    }

    private static void webAutoTest() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "E:\\Dev Tools\\browser_driver\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();
        // 与浏览器同步非常重要，必须等待浏览器加载完毕
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        webDriver.get("https://movie.douban.com/top250");
        Thread.sleep(1000);
        webDriver.quit();
    }


}
