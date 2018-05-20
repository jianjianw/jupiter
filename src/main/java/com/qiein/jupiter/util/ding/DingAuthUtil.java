package com.qiein.jupiter.util.ding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 钉钉验证相关工具类
 *
 * @Author: shiTao
 */
@Component
public class DingAuthUtil {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 获取app token 的url
     */

    private final String accessTokenUrl = "https://oapi.dingtalk.com/sns/gettoken";
    /**
     * 获取 persistentCode 的URL
     */
    private final String persistentCodeUrl = "https://oapi.dingtalk.com/sns/get_persistent_code";
    /**
     * 永久授权码 url
     */
    private final String snsTokenUrl = "https://oapi.dingtalk.com/sns/get_sns_token";
    /**
     * 获取用户信息的URL
     */
    private final String userInfoUrl = "https://oapi.dingtalk.com/sns/getuserinfo";
    /**
     * APPID
     */
    private final String appid = "dingoatz1qoekuxwg65olq";
    /**
     * SECRET
     */
    private final String secret = "bYRHSagjXrAfq0A_SRA6KhQGqGSO4qtKYv0ccExZ4Gm_ofY4nOVyqjqSgdHYvB12";


    private String accessToken;

    /**
     * 定时根据appid secret 获取应用 access token 每小时执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void timingGetAccessToken() {
        String resStr = HttpClient
                .get(accessTokenUrl)
                .queryString("appid", appid)
                .queryString("appsecret", secret)
                .asString();
        JSONObject res = JSON.parseObject(resStr);
        System.out.println(res);
        if (res.getIntValue("errcode") != 0) {
            log.error("获取钉钉 access token 失败");
        }
        log.info("获取到了access token:" + accessToken);
        this.accessToken = res.getString("access_token");
    }


    /**
     * 根据用户登录授权码和access token 获取用户持久授权码（包含openid 和 unionid）
     */
    public void getPersistentCode(String authCode) {
        if (StringUtil.isEmpty(accessToken)) {
            timingGetAccessToken();
        }
        JSONObject params = new JSONObject();
        params.put("tmp_auth_code", authCode);
        String resStr = HttpClient
                // 请求方式和请求url
                .textBody(persistentCodeUrl)
                .queryString("access_token", accessToken)
                // post提交json
                .json(params.toString())
                .asString();
        JSONObject res = JSON.parseObject(resStr);
        System.out.println(res);
    }

    /**
     * 根据access token 和 openid 。persistent_code 获取用户sns token;
     */
    public void getSnsToken(String accessToken, String openId, String persistentCode) {

    }

    /**
     * 根据  sns token 获取用户信息
     */
    public void getUserInfo(String snsToken) {

    }
}
