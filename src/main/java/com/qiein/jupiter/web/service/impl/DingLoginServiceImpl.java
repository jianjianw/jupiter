package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.service.DingLoginService;
import org.springframework.stereotype.Service;

/**
 * 钉钉登录
 *
 * @Author: shiTao
 */
@Service
public class DingLoginServiceImpl implements DingLoginService {

    /**
     * 获取TOKEN的URL
     */
    private final static String persistentCode = "https://oapi.dingtalk.com/sns/get_persistent_code";
    /**
     * 永久授权码
     */
    private final static String snsToken = "https://oapi.dingtalk.com/sns/get_sns_token";
    /**
     * 获取用户信息的URL
     */
    private final static String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
    /**
     * APPID
     */
    private final static String appid = "wx84a2df1a7858dc87";
    /**
     * SECRET
     */
    private final static String secret = "d3d9f6d809f9f03a01b2ecce5211264c";


}
