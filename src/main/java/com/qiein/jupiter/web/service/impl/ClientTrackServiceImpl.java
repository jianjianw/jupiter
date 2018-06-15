package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientTrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客资追踪业务层
 * gaoxiaoli
 */
@Service
public class ClientTrackServiceImpl implements ClientTrackService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CrmBaseApi crmBaseApi;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private NewsDao newsDao;

    /**
     * 批量删除客资
     *
     * @param kzIds
     * @param staffPO
     */
    public void batchDeleteKzList(String kzIds, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzids", kzIds);

        //获取推广人员集合
        List<StaffNumVO> onwerCollector = getOnwerStaffList("COLLECTORID", staffPO.getCompanyId(), kzIds);
        List<StaffNumVO> appoints = getOnwerStaffList("APPOINTORID", staffPO.getCompanyId(), kzIds);

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchDeleteLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            //给录入人推送消息
            pushRemoveMsg(onwerCollector, "录入", staffPO);
            //给要约人推送消息
            pushRemoveMsg(appoints, "邀约", staffPO);
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 批量转移客资
     *
     * @param kzIds
     * @param role
     * @param toStaffId
     * @param staffPO
     */
    public void batchTransferKzList(String kzIds, String role, int toStaffId, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzids", kzIds);
        reqContent.put("tostaffid", toStaffId);
        StaffPO toStaff = staffDao.getByIdAndCid(toStaffId, staffPO.getCompanyId());
        if (toStaff == null) {
            throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
        }
        reqContent.put("tostaffname", toStaff.getNickName());
        reqContent.put("role", role);

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchTransferLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            //推送给目标员工
            GoEasyUtil.pushTransfer(staffPO, toStaffId, kzIds, newsDao, clientInfoDao);
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 无效审批
     *
     * @param kzIds
     * @param memo
     * @param rst
     * @param invalidLabel
     * @param staffPO
     */
    public String approvalInvalidKzList(String kzIds, String memo, int rst, String invalidLabel, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzids", kzIds);
        reqContent.put("rst", rst);
        reqContent.put("memo", memo);
        reqContent.put("invalidLlabel", invalidLabel);

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchApprovalLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            //TODO 推送
            int wrongNum = JsonFmtUtil.strContentToJsonObj(addRstStr).getIntValue("num");
            log.info("" + wrongNum);
            return "审批成功：" + (kzIds.split(CommonConstant.STR_SEPARATOR).length - wrongNum) + "个，审批失败：" + wrongNum + "个";
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 分配客资
     *
     * @param kzIds
     * @param staffIds
     */
    public void allot(String kzIds, String staffIds) {
        //TODO 分配客资
    }

    /**
     * 批量恢复，回收客资
     *
     * @param kzIds
     * @param staffPO
     */
    public void batchRestoreKzList(String kzIds, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzids", kzIds);

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchRestoreLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if (!"100000".equals(jsInfo.getString("code"))) {
            throw new RException(jsInfo.getString("msg"));
        }
    }
}
