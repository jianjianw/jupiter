package com.qiein.jupiter;

import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * FileName: BeanTest
 *
 * @author: yyx
 * @Date: 2018-6-20 11:50
 */
public class BeanTest {
    @Test
    public void printJson(){
        GoldFingerPO goldFingerPO = new GoldFingerPO();
        goldFingerPO.setCompanyId(1);
        goldFingerPO.setStaffId(2);
        goldFingerPO.setAdId("123");
        goldFingerPO.setAdAddress("https://www.baidu.com");
        goldFingerPO.setCreateorName("小利       ");
        goldFingerPO.setPostURL("http://114.55.249.156:9091/gold_data/add_client_info");
        goldFingerPO.setIsFilter(0);
        goldFingerPO.setIsShow(0);
        goldFingerPO.setZxStyle("对话咨询");
        goldFingerPO.setTypeId(2);
        goldFingerPO.setTypeName("写真");
        goldFingerPO.setSrcId(7);
        goldFingerPO.setSrcName("支付宝口碑");
        goldFingerPO.setFieldKey("field4,field5,filed6");
        goldFingerPO.setFieldValue("kzqq,address,kzWw");
        goldFingerPO.setFormId("34654");
        goldFingerPO.setFormName("546");


        strString(goldFingerPO);
    }

    public void strString(Object obj){
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field: declaredFields){
            Class<?> type = field.getType();
            System.out.println(type.getSimpleName());
            if("String".equals(type.getSimpleName())){
            }
        }

    }
}
