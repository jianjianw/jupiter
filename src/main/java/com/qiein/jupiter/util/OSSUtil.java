package com.qiein.jupiter.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里OSS对象存储工具类，饿汉式单例模式
 * Created by Administrator on 2018/4/28 0028.
 */
public class OSSUtil {
    //参考https://blog.csdn.net/wus_shang/article/details/79286756
    //https://blog.csdn.net/github_36086968/article/details/53113274
    //=====================================永久固定的OSS常量
    /**
     * END_POINT是访问OSS的域名
     */
    private static final String END_POINT = "oss-cn-hangzhou.aliyuncs.com";
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
    private static final String BUCKET_NAME = "crm-jupiter";
    /**
     * 上传到存储空间的那个目录下
     */
    private static String dir = "image/";

    //==========================================会变的参数

    /**
     * 请求地址
     */
    private static String host = "http://" + BUCKET_NAME + "." + END_POINT;

    /**
     * 缓冲时间是三十秒,即还有三十秒要过期授权就直接废弃重新申请
     */
    private static long expireTime = 30 * 1000;

    /**
     *  临时授权过期时间
     */
    private static long expireEndTime ;

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

    /**
     * 获取Policy
     * @param callbackUrl   回调地址
     * @param seconds       授权有效持续时间
     * @return
     */
    public static JSONObject getPolicy(String callbackUrl, int seconds){
        JSONObject reply = new JSONObject();
//        //回调内容  目前并没有用到
//        Map<String, String> callback = new HashMap<>();
//        callback.put("callbackUrl", callbackUrl);
//        callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height" +
//                "=${imageInfo.height}&width=${imageInfo.width}");
//        callback.put("callbackBodyType", "application/x-www-form-urlencoded");

        //过期授权时间
        Date expiration = getExpiration(seconds);
        //oss策略对象
        PolicyConditions policyConds = new PolicyConditions();
        //设置上传文件的大小限制
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        //设置此次上传的文件名必须是dir变量的值开头
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        String postPolicy = client.generatePostPolicy(expiration, policyConds);

        //policy
        try {
            String policy = BinaryUtil.toBase64String(postPolicy.getBytes("utf-8"));
            System.out.println("policy :"+policy);
            //签名
            String signature = client.calculatePostSignature(postPolicy);
            System.out.println("signature :"+signature);
            //回调
//            String callbackData = BinaryUtil.toBase64String(callback.toString().getBytes("utf-8"));

            reply.put("accessid",ACCESS_KEY_ID);
            reply.put("policy",policy);
            reply.put("signature",signature);
            reply.put("dir",dir+getDirectory());
            reply.put("host",host);
            reply.put("expire",expiration);
//            reply.put("callback", callbackData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(reply.toString());
        return reply;
//        return reply.toJSONString();
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
    static Date getExpiration(int expire){
        if (expireEndTime == 0 || System.currentTimeMillis() > expireEndTime){    //  如果为0或者当前时间大于授权过期时间 ，则去获取
            if (expire==0)expireEndTime = System.currentTimeMillis()+expireTime;
            else expireEndTime = System.currentTimeMillis()+expire;
        }
        Date expiration = new Date(expireEndTime);
        return expiration;
    }

    /**
     * 根据时间获取目录名
     * 如：2018/05/02
     * @return
     */
    public static String getDirectory() {
        return new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
    }

    public static void main(String[] args) {
        System.out.println(getDirectory());
    }
}
