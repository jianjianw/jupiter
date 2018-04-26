package com.qiein.jupiter;

import com.alibaba.fastjson.JSONObject;
import io.goeasy.GoEasy;
import org.junit.Test;

public class GoEasyTest {

    @Test
    public void test() {
        String key = "BC-5d7407650bb74e4cae53cb782c0d760f";
        String host = "https://rest-hangzhou.goeasy.io";
        JSONObject json = new JSONObject();
        json.put("num", 10);
        json.put("time", 10);
        GoEasy goEasy = new GoEasy(host, key);
        try {
            goEasy.publish("hello", json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
