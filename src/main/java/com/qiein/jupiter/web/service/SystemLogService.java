package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.SystemLog;

/**
 * 系统日志
 *
 * @author JingChenglong 2018/05/14 16:24
 */
public interface SystemLogService {

    /**
     * 新增日志
     *
     * @param log
     */
    void addLog(SystemLog log);

    /**
     * 校验是否异常IP
     *
     * @param companyId
     * @param staffId
     * @param ip
     * @param phone
     */
    void checkAbnormalIp(int companyId, int staffId, String ip, String phone);
}