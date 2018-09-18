package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.SmsUtil;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.dao.SystemLogDao;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.entity.vo.AllotLogVO;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统日志
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogDao logDao;
    @Autowired
    private NewsDao newsDao;

    @Override
    public void addLog(SystemLog log) {
        logDao.addSystemLog(log);
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
        if (logDao.getCountByStaffIdAndIp(companyId, staffId, null) != 0
                && logDao.getCountByStaffIdAndIp(companyId, staffId, ip) == 0) {
            //提醒异地登录
            String address = HttpUtil.getIpLocation(ip);
            GoEasyUtil.pushRemoteLogin(companyId, staffId, ip, address, newsDao);
            JSONObject param = new JSONObject();
            param.put("address", address);
            //发送短信
            SmsUtil.sendAbnormalSms(companyId, phone, param);
        }
    }

    /**
     * 根据日志类型获取日志
     *
     * @param companyId
     * @param typeId
     * @return
     */
    @Override
    public PageInfo<SystemLog> getLogByType(int pageNum, int pageSize, int companyId, int typeId, int startTime, int endTime) {
        PageHelper.startPage(pageNum, pageSize);
        List<SystemLog> logByType = logDao.getLogByType(companyId, typeId, startTime, endTime);
        return new PageInfo<>(logByType);
    }

    /**
     * 定时清空日志
     *
     * @param time
     * @return
     */
    @Override
    public int clearLog(int time) {
        return logDao.clearLog(time);
    }

    /**
     * 网销排班分配日志
     */
    public List<AllotLogVO> getAllotLog(Integer companyId, Integer staffId) {
        List<AllotLogVO> allotLogVOS = logDao.getAllotLog(companyId, staffId);
        for (AllotLogVO allotLogVO : allotLogVOS) {
            if (allotLogVO.getStatus().equals(0)) {
                allotLogVO.setStatus("未领取");
            } else if (allotLogVO.getStatus().equals(1)){
                allotLogVO.setStatus("已领取");
            } else if (allotLogVO.getStatus().equals(1)) {
                allotLogVO.setStatus("已拒绝");
            }
        }
        return allotLogVOS;
    }
}