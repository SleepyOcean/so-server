package com.sleepy.common.request;

import com.sleepy.common.tools.StringTools;

/**
 * Object check
 *
 * @author gehoubao
 * @create 2021-07-10 14:09
 **/
public class Check {
    /**
     * check string is not null or empty
     *
     * @param str
     * @return
     */
    public static String checkStr(String str) {
        if (StringTools.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("checkStr failed: string is null! value = " + str);
        }
        return str;
    }
}