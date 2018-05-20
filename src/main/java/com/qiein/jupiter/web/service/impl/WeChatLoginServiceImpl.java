package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.wechat.WeChatAuthUtil;
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

    @Autowired
    private WeChatAuthUtil weChatAuthUtil;

    @Autowired
    private StaffDao staffDao;
    @Autowired
    private StaffService staffService;


    @Override
    public List<CompanyPO> forLogin(String code) {
        String openid = weChatAuthUtil.getAccessToken(code);
        StaffPO staff = staffDao.getByWeChatOpenId(openid);
        if (staff == null) {
            // 用户不存在
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        }
        List<CompanyPO> companyList = staffService.getCompanyList(staff.getPhone(), staff.getPassword(), true);

        return companyList;
    }

    @Override
    public StaffPO getCodeForIn(String code, String openid, int companyId, String ip) {
        // TODO Auto-generated method stub
        StaffPO staff = staffDao.getByWeChatOpenId(openid);
        staffService.loginWithCompanyId(staff.getPhone(), staff.getPassword(), companyId, ip, true);
        return staff;
    }


}

