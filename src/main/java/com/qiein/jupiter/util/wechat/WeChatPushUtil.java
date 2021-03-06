package com.qiein.jupiter.util.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    //发送模版
    private static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    //APPID
    private final static String APP_ID = "wxcfb9db7577fca934";   //"wx67a4a7028f12a820"
    //APPSECRET
    private final static String APP_SECRET = "c00e1dc5cf3c7c305ccf9e0b9dd6158e";    //"f1643abe4865080db153a0d181719005"
    //换行
    private final static String NEW_LINE = "\r\n";
    //阿波罗地址
    public static String APOLLO_URL;

    @Value("${apollo.baseUrl}")
    public void setApolloUrl(String apolloUrl) {
        WeChatPushUtil.APOLLO_URL = apolloUrl;
    }

    //    private final static String APOLLO_URL ="http://127.0.0.1:80/";
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
        String resJsonStr = HttpClient.get(APOLLO_URL+"/wechat/get_access_token").asString();
        JSONObject resJsonObj = JSONObject.parseObject(resJsonStr);
        if (resJsonObj.getIntValue("code") !=  SUCCESS_CODE){
            //TODO  返回失败,做处理
            System.out.println(resJsonObj.getString("msg"));
        }
        System.out.println("AccessToken: "+resJsonObj.getString("data"));
        return resJsonObj.getString("data");
    }

    /**
     * 获取二维码地址url
     * @param staffId
     * @param companyId
     * @return
     */
    public static String getQRCodeImg(Integer staffId,Integer companyId){
        return GET_QR_CODE+getQRCode(staffId, companyId).getTicket();
    }

    /**
     * 获取临时二维码对象
     * ticket 用于换取二维码
     * expire_seconds 二维码的有效时间
     * url  二维码解析后的地址
     * @return
     */
    public static QRCodeDTO getQRCode(Integer staffId,Integer companyId){  //GET_QR_CODE_INFO+getAccessToken()
        Map<String,Object> scene_str = new HashMap<String,Object>();
        String cidAndUid = "cid_"+companyId+"&uid_"+staffId;    //+"&url_"+APOLLO_URL.replace("http://","")
        scene_str.put("scene_str",cidAndUid);
        Map<String,Object> scene = new HashMap<String,Object>();
        scene.put("scene",scene_str);
        return getQRCode(60*5,"QR_STR_SCENE",scene);
    }

    /**
     * 获取二维码信息（可永久亦可临时）
     * @param expire_seconds
     * @param action_name
     * @param action_info
     * @return
     */
    public static QRCodeDTO getQRCode (int expire_seconds,String action_name,Map action_info){  //GET_QR_CODE_INFO+getAccessToken()
        JSONObject params = new JSONObject();
        params.put("expire_seconds",expire_seconds);
        params.put("action_name",action_name);
        params.put("action_info",action_info);
        System.out.println("请求微信的JSON字符串： "+params.toJSONString());
        String resJsonStr = HttpClient.textBody(GET_QR_CODE_INFO+getAccessToken())
                .json(params.toString())
                .asString();
        System.out.println(resJsonStr);
        //TODO 异常处理
        QRCodeDTO qrCodeDTO = JSONObject.parseObject(resJsonStr,QRCodeDTO.class);
        System.out.println(qrCodeDTO);
        return qrCodeDTO;
    }

//    //TODO 推送新客资模版消息
//    public static void pushMsg(WeChatPushMsgDTO weChatPushMsgDTO){
//        String resJsonStr = HttpClient.textBody(SEND_TEMPLATE_MESSAGE+getAccessToken())
//                .json(JSON.toJSONString(weChatPushMsgDTO))
//                .asString();
//        System.out.println(resJsonStr);
//        JSONObject resJson = JSONObject.parseObject(resJsonStr);
//        if (resJson.getIntValue("errcode")!=0){
//            //TODO 之后日志记录
//            System.out.println("微信消息推送失败");
//        }
//    }

    public static void removeBind(Integer companyId,Integer staffId){
        String url = APOLLO_URL+"/wechat/remove_bind";
        String resultJsonStr = HttpClient.get(url)
                .queryString("companyId",companyId)
                .queryString("staffId",staffId)
                .asString();
        JSONObject resultJson = JSONObject.parseObject(resultJsonStr);
        if (resultJson.getIntValue("code")!=SUCCESS_CODE){
            throw new RException(ExceptionEnum.WX_REMOVE_BIND_FAIL);
        }
    }

    /**
     * 推送新客资模版消息
     * @param weChatPushMsgDTO
     */
    public static void pushNewClientMsg(WeChatPushMsgDTO weChatPushMsgDTO){
        String contentStr = JSONObject.toJSONString(weChatPushMsgDTO);
        //TODO 之后放进配置类中
        String url = APOLLO_URL+"/wechat/push_new_client";
        HttpClient.textBody(url)
                .json(contentStr)
                .execute();
    }

    /**
     * 推送普通客资信息
     * @param weChatPushMsgDTO
     */
    public static void pushMsg(WeChatPushMsgDTO weChatPushMsgDTO){
        String contentStr = JSON.toJSONString(weChatPushMsgDTO);
        String url = APOLLO_URL+"/wechat/push_client_msg";
        HttpClient.textBody(url)
                .json(contentStr)
                .execute();
    }




    // 新客资消息推送 DEMO
    public static void main(String[] args) {
        String firstText = "客资变更通知";
        String remarkText = "状态： 待领取"+NEW_LINE+"配偶: 丧偶"+NEW_LINE+"咨询类型： 婚纱照";
        WeChatPushMsgDTO weChatPushMsgDTO = new WeChatPushMsgDTO(firstText,remarkText,5001,"函木婚纱",30191,"猪猪","15888888888","2018年6月8日 21:46");
        pushMsg(weChatPushMsgDTO);
    }

    }
