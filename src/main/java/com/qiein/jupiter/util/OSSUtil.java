package com.qiein.jupiter.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PolicyConditions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

/**
 * 阿里OSS对象存储工具类，饿汉式单例模式
 * Created by Administrator on 2018/4/28 0028.
 */
public class OSSUtil {

    //=====================================永久固定的OSS常量
    /**
     * END_POINT是访问OSS的域名
     */
    private static final String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com/";
    /**
     * 访问密钥的id
     */
    private static final String ACCESS_KEY_ID = "LTAIH4J10mrJgons";
    /**
     * 访问密钥
     */
    private static final String ACCESS_KEY_SECRET = "9wSrBw0DKHHY0YKBA1FEGkch6rNEVb";

    //========================================存储文件相对的OSS地址
    /**
     * 存储空间名称
     */
    private static final String BUCKET_NAME = "jupiter-crm";
    /**
     * 上传到存储空间的那个目录下
     */
    private static String dir = "image";

    //==========================================会变的参数

    /**
     * 请求地址
     */
    private static String host = "http://" + BUCKET_NAME + "." + END_POINT;

    /**
     * 缓冲时间是三十秒,即还有三十秒要过期授权就直接废弃重新申请
     */
    private long expireTime = 30 * 1000;

    /**
     *  临时授权过期时间
     */
    private long expireEndTime ;

    //==================================================私有对象
    /**
     * 阿里云OSS云存储对象
     */
    private static OSSClient client = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

    /**
     * OSS工具类对象
     */
    private static OSSUtil ossUtil = new OSSUtil();

    /**
     * 私有构造函数，无法直接new，只能通过getInstance方法获取唯一实例
     */
    private OSSUtil(){

    }

    /**
     * 获取单例
     * @return
     */
    public static OSSUtil getInstance(){
        return ossUtil;
    }

    //获取Policy
    public static PolicyConditions getPolicy(){
        return null;
    }

    /**
     * 大概是
     *
     * @param request
     * @param response
     * @param results   签名内容的JSON化之后的字符串
     * @throws IOException
     */
    private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
        //获取请求中的回调参数
        String callbackFunName = request.getParameter("callback");

        if (callbackFunName==null || callbackFunName.equalsIgnoreCase(""))
            //如果回调参数不为空 字符输出流输出字符串
            response.getWriter().println(results);
        else
            //如果为空
            response.getWriter().println(callbackFunName + "( "+results+" )");
        //设置响应状态200，成功
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    /**
     * 获取过期授权时间
     * @return
     */
    Date getExpiration(){
        if (expireEndTime == 0 || System.currentTimeMillis() > expireEndTime){    //  如果为0或者当前时间大于授权过期时间 ，则去获取
            expireEndTime = System.currentTimeMillis()+expireTime;
        }
        Date expiration = new Date(expireEndTime);
        return expiration;
    }

}
