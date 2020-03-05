package com.sleepy.common.tools;

import com.sleepy.common.exception.UserOperationIllegalException;
import com.sleepy.common.model.MapModel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 公用工具
 *
 * @author gehoubao
 * @create 2020-03-05 18:25
 **/
public class CommonTools {

    /**
     * 抛出指定信息的异常
     *
     * @param info
     * @return
     * @throws Exception
     */
    public static boolean throwExceptionInfo(String info) throws Exception {
        throw new Exception(info);
    }

    /**
     * 抛出用户操作的异常
     *
     * @param info
     * @return
     * @throws UserOperationIllegalException
     */
    public static boolean throwUserExceptionInfo(String info) throws UserOperationIllegalException {
        throw new UserOperationIllegalException(info);
    }

    /**
     * 获取自定义Map
     *
     * @param extras
     * @return
     */
    public static Map<String, Object> getCustomMap(MapModel... extras) {
        Map<String, Object> map = new HashMap<>(extras.length);
        for (MapModel mapModel : extras) {
            map.put(mapModel.getKey(), mapModel.getValue());
        }
        return map;
    }

    /**
     * 格式化价格
     *
     * @param price
     * @return
     */
    public static Double formatPriceNum(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return new Double(df.format(price).toString());
    }
}