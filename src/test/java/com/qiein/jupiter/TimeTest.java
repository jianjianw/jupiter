package com.qiein.jupiter;

import com.qiein.jupiter.util.TimeUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
public class TimeTest {
    @Test
    public void getUtcTime(){
        //Sat Jun 24 20:45:09 CST 2017

        String str = "Sat Jun 24 20:45:10 CST 2017";
        String str1 = "2017-6-24  20:45:09";
        String str2 = "2017-06-24  20:45";
        String str3 = "2017-6-24  20:45";
        String str4 = "2017-06-24  8:45:09";
        try{
            Date date = TimeUtil.smartFormat(str4);
            System.out.println(date);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
