package com.qiein.jupiter;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.qiein.jupiter.enums.WebSocketMsgEnum;
import com.qiein.jupiter.web.entity.dto.WebSocketMsgDTO;
import org.apache.http.HttpEntity;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

public class AllTest {


    @Test
    public void test() {
        List list = new ArrayList();
        System.out.println(list.isEmpty());

    }

    public static void main(String[] args) {
        System.out.println(getMap().get("KEY"));
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("KEY", "INIT");
        try {
            map.put("KEY", "TRY");
            return map;
        } catch (Exception e) {
            map.put("KEY", "CATCH");
        } finally {
            map.put("KEY", "FINALLY");
            map = null;
        }
        return map;
    }

    public static int getInt() {
        int a = 0;
        try {
            a = 1;
            return a;
        } catch (Exception e) {
            a = 2;
        } finally {
            a = 3;
        }
        return a;
    }

    public static String getString() {
        String a = "0";
        try {
            a = "1";
            return a;
        } catch (Exception e) {
            a = "2";
        } finally {
            a = "3";
        }
        return a;
    }

    @Test
    public void testTime(){
        Long time=43223L;
        Date date=new Date(time);
        Date javaDate = HSSFDateUtil.getJavaDate(time);
        System.out.println(javaDate);
    }

    @Test
    public void testEnum(){
        WebSocketMsgDTO companyMsgDTO = new WebSocketMsgDTO();
        companyMsgDTO.setCompanyId(1);
        companyMsgDTO.setContent("hhhhh");
        companyMsgDTO.setType(WebSocketMsgEnum.OrderSuccess);
        System.out.println(companyMsgDTO.getType());
        System.out.println(JSONObject.toJSONString(companyMsgDTO));
    }
}
