package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.SmsUtil;
import com.qiein.jupiter.web.controller.ApolloController;
import com.qiein.jupiter.web.dao.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.SystemLogDao;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.SystemLogService;

import java.util.HashMap;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogDao logDao;
    @Autowired
    private NewsDao newsDao;
    //异地登录短信模板ID
    private static final String abnormalTemplateId = "SMS_136399066";

    @Override
    public void addLog(SystemLog log) {
        logDao.addSystemLog(DBSplitUtil.getSystemLogTabName(log.getCompanyId()), log);
    }

    /**
     * 校验是否异常IP
     *
     * @param companyId
     * @param staffId
     * @param ip
     * @param phone
     */
    public void checkAbnormalIp(int companyId, int staffId, String ip, String phone) {
        //如果是异常登录，推送该消息
        if (logDao.getCountByStaffIdAndIp(DBSplitUtil.getSystemLogTabName(companyId), companyId, staffId, ip) == 0) {
            //提醒异地登录
            String address = HttpUtil.getIpLocation(ip);
            GoEasyUtil.pushRemoteLogin(companyId, staffId, ip, address, newsDao);
            JSONObject param = new JSONObject();
            param.put("ip", ip);
            param.put("address", address);
            //发送短信
            SmsUtil.sendSms(companyId, abnormalTemplateId, phone, param.toJSONString());
        }
    }
}