package com.qiein.jupiter.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.qiein.jupiter.exception.RException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果解析工具类
 *
 * @author JingChenglong 2016-09-10 16:32
 */
public class JsonFmtUtil {

    static DecimalFormat fmt = new DecimalFormat("0.00");

    /*-- 字符串转JSON --*/
    public static JSONObject strToJsonObj(String str) throws RException {

        JSONObject res = (JSONObject) JSONObject.parse(str);

        if ("100000".equals(res.getJSONObject("response").getJSONObject("info").get("code"))) {
            return res.getJSONObject("response").getJSONObject("content");
        } else {
            throw new RException(res.getJSONObject("response").getJSONObject("info").getString("msg"));
        }
    }


    /*-- 获取返回结果JSON --*/
    public static JSONObject strInfoToJsonObj(String str) throws RException {

        JSONObject res = (JSONObject) JSONObject.parse(str);

        return res.getJSONObject("response").getJSONObject("info");
    }

    /*-- 获取返回内容JSON --*/
    public static JSONObject strContentToJsonObj(String str) throws RException {

        JSONObject res = (JSONObject) JSONObject.parse(str);

        return res.getJSONObject("response").getJSONObject("content");
    }



}