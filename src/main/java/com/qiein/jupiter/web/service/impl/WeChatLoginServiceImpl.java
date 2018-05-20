package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import com.qiein.jupiter.web.service.WeChatLoginService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: shiTao
 */
@Service
public class WeChatLoginServiceImpl implements WeChatLoginService {
    /**
     * 获取TOKEN的URL
     */
    private final static String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
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

    @Autowired
    private StaffDao staffDao;
    @Autowired
    private StaffService staffService;
    @Override
    public StaffDetailPO getAccessToken(String code) {
        String wechatRes = HttpClient
                .get(tokenUrl)
                .queryString("appid", appid)
                .queryString("secret", secret)
                .queryString("code", code)
                .queryString("grant_type", "authorization_code")
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        System.out.println(res);
        StaffDetailPO staffDetailPO=getUserInfo(res.getString("access_token"), res.getString("openid"));
        return staffDetailPO;
    }

    @Override
    public StaffDetailPO getUserInfo(String token, String openId) {
    	StaffDetailPO staffDetailPO=new StaffDetailPO();
    	staffDetailPO.setOpenId(openId);
        String wechatRes = HttpClient
                .get(userInfoUrl)
                .queryString("access_token", token)
                .queryString("openid", openId)
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        System.out.println(res);
        staffDetailPO.setWeChatName(res.getString("nickname"));
        staffDetailPO.setWeChatImg(res.getString("headimgurl"));
        return staffDetailPO;
        
    }

	@Override
	public List<CompanyPO> forLogin(String code) {
		// TODO Auto-generated method stub
		String wechatRes = HttpClient
                .get(tokenUrl)
                .queryString("appid", appid)
                .queryString("secret", secret)
                .queryString("code", code)
                .queryString("grant_type", "authorization_code")
                .asString();
        JSONObject res = JSON.parseObject(wechatRes);
        System.out.println(res);
        String openid= res.getString("openid");
        StaffPO staff=staffDao.getByWeChatOpenId(openid);
        if (staff==null) {
            // 用户不存在
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        }
        List<CompanyPO> companyList =staffService.getCompanyList(staff.getPhone(), staff.getPassword(),true);
       
        return companyList;
	}

	@Override
	public StaffPO getCodeForIn(String code,String openid, int companyId, String ip) {
		// TODO Auto-generated method stub
		
        StaffPO staff=staffDao.getByWeChatOpenId(openid);
        staffService.loginWithCompanyId(staff.getPhone(), staff.getPassword(), companyId, ip, true);
		return staff;
	}
	
	
	
}

