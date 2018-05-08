package com.qiein.jupiter;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.qiein.jupiter.enums.OrderSuccessTypeEnum;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.enums.WebSocketMsgEnum;
import com.qiein.jupiter.web.entity.dto.OrderSuccessMsg;
import com.qiein.jupiter.web.entity.dto.WebSocketMsgDTO;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.junit.Test;

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
        System.out.println(TigMsgEnum.ADD_BRAND_SUCCESS);
    }

    @Test
    public void testMsg(){
        OrderSuccessMsg orderSuccessMsg=new OrderSuccessMsg();
        orderSuccessMsg.setCompanyId(1);
        orderSuccessMsg.setStaffName("张三1");
        orderSuccessMsg.setShopName("三亚");
        orderSuccessMsg.setAmount("12000");
        orderSuccessMsg.setType(OrderSuccessTypeEnum.ArrivalShop);
        orderSuccessMsg.setSrcImg("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=156581925,3170808657&fm=27&gp=0.jpg");
        orderSuccessMsg.setHeadImg("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4005596794,992112216&fm=27&gp=0.jpg");
        WebSocketMsgDTO webSocketMsgDTO=new WebSocketMsgDTO();
        webSocketMsgDTO.setCompanyId(1);
        webSocketMsgDTO.setType(WebSocketMsgEnum.OrderSuccess);
        webSocketMsgDTO.setContent(JSONObject.toJSONString(orderSuccessMsg));

        System.out.println(JSONObject.toJSONString(webSocketMsgDTO));
    }
}
