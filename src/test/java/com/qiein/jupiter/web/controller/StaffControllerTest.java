package com.qiein.jupiter.web.controller;

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
        if (!"100000".equals(jsInfo.getString("code"))){
            System.out.println(jsInfo.get("content.result"));
        }
        System.out.println(back);
    }

}