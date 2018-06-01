package com.qiein.jupiter.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;

/**
 * Created by Tt(叶华葳)
 * on 2018/5/24 0024.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StaffControllerTest {

    @Autowired
    private CrmBaseApi crmBaseApi;
    @Test
    public void getAll() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("companyid",1);
        map.put("oldstaffid",712);
        String back = crmBaseApi.doService(map,"staffCanBeDelete");

        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(back);
        JSONObject jsCent = JsonFmtUtil.strContentToJsonObj(back);
//        JSONObject jsInfo = JSON.parseObject(back);
        System.out.println(jsInfo.toJSONString());
        System.out.println(jsCent.toJSONString());
//        if (!"100000".equals(jsInfo.getString("code"))){
//            System.out.println("result:"+jsInfo.getJSONObject("response").getJSONObject("info").getJSONObject("content").getBoolean("result"));
//        }
//        System.out.println("result:"+jsInfo.getJSONObject("response").getJSONObject("info").getJSONObject("content").getBoolean("result"));
    }

    @Test
    public void asd() throws Exception{
        String ids = "1,2";
        if (ids.split(",").length>1){
            System.out.println(true);
        }
        System.out.println(false);
    }
}