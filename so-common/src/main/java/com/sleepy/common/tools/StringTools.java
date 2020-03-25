package com.sleepy.common.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author gehoubao
 * @create 2019-04-25 13:44
 **/
public class StringTools {
    public static final String QUESTION_MARK = "?";
    public static final String POINT = ".";
    public static final String AND = "&";
    public static final String EQUAL = "=";
    public static final String NO_EQUAL = "<>";
    public static final String PARENTHESES_LEFT = "(";
    public static final String PARENTHESES_RIGHT = ")";
    public static final String COMMA = ",";
    public static final String OR_MARK = "OR";
    public static final String AND_MARK = "AND";
    public static final String FORWARD_SLASH = "/";
    public static final String BACK_SLASH = "\\";

    public static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isNullOrEmpty(String string) {
        if (null == string || "".equals(string)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param string
     * @return
     */
    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    /**
     * 美化json字符串
     *
     * @param json
     * @return 美化后的json字符串
     * @throws IOException
     */
    public static String formatJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = null;
        obj = mapper.readValue(json, Object.class);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * 格式化http请求URL
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String formatUrl(String url) throws UnsupportedEncodingException {
        url = URLDecoder.decode(url, "utf-8").replaceAll("&amp;", "&");
        String dest = url;
        Pattern pat = CHINESE_PATTERN;
        Matcher mat = pat.matcher(url);
        while (mat.find()) {
            String s = mat.group();
            dest = dest.replaceAll(s, URLEncoder.encode(s, "utf-8"));
        }
        return dest;
    }

    /**
     * 获取不带‘-’的随机UUID
     *
     * @param intervalMark
     * @return
     */
    public static String getRandomUuid(String intervalMark) {
        return UUID.randomUUID().toString().replaceAll("-", intervalMark);
    }

    /**
     * 格式化文件大小，输入文件大小（byte为单位），输出带单位的文件大小，如 10240 => 10M
     *
     * @param size
     * @return
     */
    public static String getFormatFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = BigDecimal.valueOf(kiloByte);
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = BigDecimal.valueOf(megaByte);
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = BigDecimal.valueOf(gigaByte);
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 从数字和字符组成的字符串中获取整数， 如 1024 bytes => 1024
     *
     * @param numStr
     * @return
     */
    public static int getIntegerNumFromString(String numStr) {
        String trimStr = numStr.replaceAll("[^0-9]", "").trim();
        int value = Integer.parseInt(StringTools.isNullOrEmpty(trimStr) ? "0" : trimStr);
        return value;
    }

    /**
     * 获取指定位数的随机数字字符串
     *
     * @param bit
     * @return
     */
    public static String getRandomNumString(int bit) {
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bit; i++) {
            s.append(random.nextInt(9));
        }
        return s.toString();
    }

    /**
     * 使用符号拼接多个字符串
     *
     * @param splitSymbol
     * @param strings
     * @return
     */
    public static String getSplitString(String splitSymbol, String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
            sb.append(splitSymbol);
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String replaceIgnoreCase(String source, String reg, String replacement) {
        Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        String r = m.replaceAll(replacement);
        return r;
    }
}