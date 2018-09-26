package com.qiein.jupiter;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @Author: shiTao
 */
public class AllotTest {

    @Test
    public void test() {

        //先找出当前在线客服放入队列
        //
        String config = "";
        JSONObject configJson = JSONObject.parseObject(config);
        if (configJson.getBoolean("autoAllot") == null) {

            return;
        }

    }
}
