package com.qiein.jupiter.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.util.OkHttpUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @create by Tt(叶华葳) 2018-06-05 15:55
 */
@Component
public class WeChanPushUtil {

    //GET请求，获取access_token
    private static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx67a4a7028f12a820&secret=f1643abe4865080db153a0d181719005";
                                                // "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx67a4a7028f12a820&secret=f1643abe4865080db153a0d181719005"
    //APPID
    private final static String APP_ID = "wxcfb9db7577fca934";   //"wx67a4a7028f12a820"
    //APPSECRET
    private final static String APP_SECRET = "c00e1dc5cf3c7c305ccf9e0b9dd6158e";    //"f1643abe4865080db153a0d181719005"

    private static String accessToken ;
    private static long expireTime ;

    /**
     * 如果为AccessToken的有效期内，则直接从内存获取。
     * 如果即将超时，则去微信方获取。
     * @return
     */
    public static String getAccessToken (){
        //如果存在accessToken 并且 当前有效
        if (accessToken !=null && expireTime > System.currentTimeMillis()){
            return accessToken;
        }
        String accessToken = null;
        try {
            System.out.println(GET_ACCESS_TOKEN);
            accessToken = OkHttpUtil.doGet(GET_ACCESS_TOKEN).getString("access_token");
            expireTime = System.currentTimeMillis() + 2*55*1000;
            System.out.println("expireTime: "+expireTime);
            System.out.println("accessToken: "+accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public static void main(String[] args) {
        getAccessToken();
    }

    //TODO 之后不要写死URL
    public static String getAccessTokenUrl (){
        String accessTokenUrl = GET_ACCESS_TOKEN.toString();
        return accessTokenUrl.replaceFirst("/$APPID/$",APP_ID).replaceFirst("/$APPSECRET/$",APP_SECRET);
    }


}
