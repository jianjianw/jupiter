package com.qiein.jupiter.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @create by Tt(叶华葳) 2018-06-05 15:55
 */
@Component
public class WeChatPushUtil {

    //GET请求，获取access_token
    private static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx67a4a7028f12a820&secret=f1643abe4865080db153a0d181719005";
                                                // "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx67a4a7028f12a820&secret=f1643abe4865080db153a0d181719005"
    //获取二维码信息
    private static final String GET_QR_CODE_INFO = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    //获取二维码图片
    private static final String GET_QR_CODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    //APPID
    private final static String APP_ID = "wxcfb9db7577fca934";   //"wx67a4a7028f12a820"
    //APPSECRET
    private final static String APP_SECRET = "c00e1dc5cf3c7c305ccf9e0b9dd6158e";    //"f1643abe4865080db153a0d181719005"
    //阿波罗地址
    private final static String APOLLO_URL ="http://ftq794.natappfree.cc/";
    //成功code
    private final static int SUCCESS_CODE = 100000;

    private static String accessToken ;
    private static long expireTime ;

//    /**
//     * 如果为AccessToken的有效期内，则直接从内存获取。
//     * 如果即将超时，则去微信方获取。
//     * @return
//     */
//    public static String getAccessToken (){
//        //如果存在accessToken 并且 当前有效
//        if (accessToken !=null && expireTime > System.currentTimeMillis()){
//            return accessToken;
//        }
//        String accessToken = null;
//        try {
//            System.out.println(GET_ACCESS_TOKEN);
//            accessToken = OkHttpUtil.doGet(GET_ACCESS_TOKEN).getString("access_token");
//            expireTime = System.currentTimeMillis() + 2*55*1000;
//            System.out.println("expireTime: "+expireTime);
//            System.out.println("accessToken: "+accessToken);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return accessToken;
//    }

    /**
     *
     * @return
     */
    public static String getAccessToken(){
        //如果存在accessToken 并且 当前有效
        if (accessToken !=null && expireTime > System.currentTimeMillis()){
            return accessToken;
        }
        String resJsonStr = HttpClient.get(APOLLO_URL+"wechat/get_access_token").asString();
        JSONObject resJsonObj = JSONObject.parseObject(resJsonStr);
        if (resJsonObj.getIntValue("code") !=  SUCCESS_CODE){
            //TODO  返回失败,做处理
            System.out.println(resJsonObj.getString("msg"));
        }
        return resJsonObj.getString("data");
    }

    public static void main(String[] args) {
        getQRCode();
    }

    /**
     * 获取临时二维码对象
     * ticket 用于换取二维码
     * expire_seconds 二维码的有效时间
     * url  二维码解析后的地址
     * @return
     */
    public static QRCodeDTO getQRCode(){  //GET_QR_CODE_INFO+getAccessToken()
        JSONObject scene = new JSONObject();
        scene.put("scene_id","123");
        return getQRCode(300,"QR_SCENE",scene.toString());
    }

    /**
     * 获取二维码信息（可永久亦可临时）
     * @param expire_seconds
     * @param action_name
     * @param scene_id
     * @return
     */
    public static QRCodeDTO getQRCode (int expire_seconds,String action_name,String scene_id){  //GET_QR_CODE_INFO+getAccessToken()
        JSONObject params = new JSONObject();
        params.put("expire_seconds",expire_seconds);
        params.put("action_name",action_name);
        params.put("scene",scene_id);
        String resJsonStr = HttpClient.textBody(GET_QR_CODE_INFO+getAccessToken())
                .json(params.toString())
                .asString();
        QRCodeDTO qrCodeDTO = JSONObject.parseObject(resJsonStr,QRCodeDTO.class);
        System.out.println(qrCodeDTO);
        return qrCodeDTO;
    }

    //TODO 之后不要写死URL
    public static String getAccessTokenUrl (){
        String accessTokenUrl = GET_ACCESS_TOKEN.toString();
        return accessTokenUrl.replaceFirst("/$APPID/$",APP_ID).replaceFirst("/$APPSECRET/$",APP_SECRET);
    }


}
