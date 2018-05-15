package com.qiein.jupiter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取配置文件properties工具类
 * Created by Tt on 2018/5/15 0015.
 */
public class PropertiesUtil {

    /**
     * 基础配置文件
     */
    private final static String BASE_PROPERTIES ="application.properties";

    /**
     * 当前分支
     */
    private static String currentBranch;

    /**
     * 当前配置文件
     */
    private static String currentBranchProperties;

    //多个配置文件是可用map或者list存入文件名
//    private Map<String,String> propertiesMap;

    static{
        init();
    }

    private PropertiesUtil (){}

    public static void main(String[] args) {

    }
    public static void init(){
        if (currentBranch == null){
            //初始化
            Properties properties = new Properties();
            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(BASE_PROPERTIES);
            // 使用properties对象加载输入流
            try {
                properties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取key对应的value值
            currentBranch = properties.getProperty("spring.profiles.active");
            currentBranchProperties = BASE_PROPERTIES.substring(0,BASE_PROPERTIES.lastIndexOf("."))+"-"+currentBranch+"."+BASE_PROPERTIES.substring(BASE_PROPERTIES.lastIndexOf(".")+1);
        }
    }

    /**
     * 根据key获取配置文件中对应的value，会先找application-xxx.properties文件，然后查询application.properties文件
     * @param key
     * @return
     */
    public static String getValue(String key){
        String value = null;
        if (StringUtil.isNotEmpty(currentBranch)){  //  如果不为空先取当前
            value = getValue(key,currentBranchProperties);
        }else{
            value = getValue(key,BASE_PROPERTIES);
        }
        return value;
    }

    /**
     * 根据key和文件路径获取对应的value，默认是类加载器的相对地址
     * @param key
     * @param propertiesName
     * @return
     */
    private static String getValue(String key , String propertiesName){
        String value =null;
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        value = properties.getProperty(key);
        return value;
    }

}
