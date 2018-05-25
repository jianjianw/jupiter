package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/5/24 0024.
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
        map.put("oldstaffid",2);
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