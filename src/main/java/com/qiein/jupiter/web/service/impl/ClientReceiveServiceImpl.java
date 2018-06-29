package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.StaffStatusEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.po.StaffStatusLog;
import com.qiein.jupiter.web.service.ClientReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客资领取
 *
 * @author JingChenglong 2018/05/09 17:09
 */
@Service
public class ClientReceiveServiceImpl implements ClientReceiveService {

    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private ClientAllotLogDao clientAllotLogDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private StaffStatusLogDao statusLogDao;

    @Override
    @Transactional
    public void receive(String kzId, String logId, int companyId, int staffId, String staffName) {
        if (StringUtil.haveEmpty(kzId, logId) || NumUtil.haveInvalid(companyId, staffId)) {
            throw new RException(ExceptionEnum.INFO_ERROR);
        }
        if (kzId.length() > 32 && kzId.indexOf(CommonConstant.STR_SEPARATOR) != -1) {
            // 多个客资领取
            receive(kzId.split(CommonConstant.STR_SEPARATOR), logId.split(CommonConstant.STR_SEPARATOR), companyId,
                    staffId, staffName);
        } else {
            // 一个客资领取
            receive(kzId, Integer.valueOf(logId), companyId, staffId, staffName);
        }

        // 计算今日客资个数
        resizeTodayNum(companyId, staffId);

        // 推送页面重载客资列表
        GoEasyUtil.pushInfoRefresh(companyId, staffId);
    }

    /**
     * 客资拒接
     */
    @Override
    public void refuse(String kzId, String logId, int companyId, int staffId, String staffName) {
        if (StringUtil.haveEmpty(kzId, logId) || NumUtil.haveInvalid(companyId, staffId)) {
            throw new RException(ExceptionEnum.INFO_ERROR);
        }
        if (kzId.length() > 32 && kzId.indexOf(CommonConstant.STR_SEPARATOR) != -1) {
            // 多个客资拒接
            String[] logIds = logId.split(CommonConstant.STR_SEPARATOR);
            for (String id : logIds) {
                refuse(kzId, Integer.valueOf(id), companyId, staffId, staffName);
            }
        } else {
            // 一个客资拒接
            refuse(kzId, Integer.valueOf(logId), companyId, staffId, staffName);
        }
    }

    /**
     * 拒接客资
     *
     * @param kzId
     * @param logId
     * @param companyId
     * @param staffId
     * @param staffName
     */
    private void refuse(String kzId, int logId, int companyId, int staffId, String staffName) {
        // 修改客资分配日志状态为已拒绝
        int updateNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId,
                logId, ClientConst.ALLOT_LOG_STATUS_REFUSE, "");
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.ALLOT_LOG_ERROR);
        }
    }

    /**
     * 领取一个客资
     *
     * @param kzId
     * @param logId
     * @param companyId
     * @param staffId
     */
    @Transactional
    public void receive(String kzId, int logId, int companyId, int staffId, String staffName) {

        ClientPushDTO info = clientInfoDao.getClientPushDTOById(kzId, DBSplitUtil.getInfoTabName(companyId),
                DBSplitUtil.getDetailTabName(companyId));
        if (info == null) {
            throw new RException(ExceptionEnum.INFO_ERROR);
        }
        if (staffId != info.getAppointorId()) {
            throw new RException(ExceptionEnum.INFO_OTHER_APPOINTOR);
        }
        if (ClientStatusConst.BE_ALLOTING != info.getStatusId()
                && ClientStatusConst.BE_WAIT_MAKE_ORDER != info.getStatusId()) {
            throw new RException(ExceptionEnum.INFO_BE_RECEIVED);
        }

        // 修改客资信息
        updateInfoWhenReceive(companyId, kzId, logId, staffId, staffName);
    }

    /**
     * 领取时修改客资信息
     *
     * @param companyId
     * @param kzId
     * @param logId
     * @param staffId
     * @param staffName
     */
    @Transactional
    public void updateInfoWhenReceive(int companyId, String kzId, int logId, int staffId, String staffName) {
        // 修改客资状态为未设置
        int updateNum = clientInfoDao.updateClientInfoStatus(companyId, DBSplitUtil.getInfoTabName(companyId), kzId,
                ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_HAVE_MAKE_ORDER);
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
        }

        // 修改客资的领取时间和最后操作时间
        updateNum = clientInfoDao.updateClientInfoAfterAllot(companyId, DBSplitUtil.getInfoTabName(companyId), kzId);
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.INFO_EDIT_ERROR);
        }

        // 添加客资领取日志
        updateNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId), new ClientLogPO(kzId, staffId,
                staffName, ClientLogConst.INFO_LOG_RECEIVE, ClientLogConst.INFO_LOGTYPE_RECEIVE, companyId));
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.LOG_ERROR);
        }

        // 修改客资分配日志状态为已领取
        updateNum = clientAllotLogDao.updateAllogLog(DBSplitUtil.getAllotLogTabName(companyId), companyId, kzId, logId,
                ClientConst.ALLOT_LOG_STATUS_YES, "now");
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.ALLOT_LOG_ERROR);
        }
    }

    /**
     * 计算员工今日客资数，并校验是否满限状态
     *
     * @param companyId
     * @param staffId
     */
    private void resizeTodayNum(int companyId, int staffId) {
        // 计算客服今日领取客资数
        int num = staffDao.getTodayKzNum(companyId, staffId, DBSplitUtil.getInfoTabName(companyId));
        // 修改今日领取客资数
        int updateNum = staffDao.updateTodatKzNum(companyId, staffId, num);
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
        }
        // 计算是否满限
        updateNum = staffDao.checkOverFlowToday(companyId, staffId);
        if (1 == updateNum) {
            // 记录状态修改日志
            statusLogDao.insert(
                    new StaffStatusLog(staffId, StaffStatusEnum.LIMIT.getStatusId(), CommonConstant.SYSTEM_OPERA_ID,
                            CommonConstant.SYSTEM_OPERA_NAME, companyId, ClientLogConst.LIMITDAY_OVERFLOW));
            // 推送状态重载消息
            GoEasyUtil.pushStatusRefresh(companyId, staffId);
        }
    }

    /**
     * 领取多个客资
     */
    private void receive(String[] kzIdArr, String[] logIdArr, int companyId, int staffId, String staffName) {
        for (int i = 0; i < kzIdArr.length; i++) {
            receive(kzIdArr[i], Integer.valueOf(logIdArr[i]), companyId, staffId, staffName);
        }
    }
}