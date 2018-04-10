package com.qiein.jupiter.util;

import com.alibaba.druid.filter.config.ConfigTools;

public class CreateDruidPwd {

    public static  void  main(String [] args){
        String []  pwd={""};
        try {
            ConfigTools.main(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}