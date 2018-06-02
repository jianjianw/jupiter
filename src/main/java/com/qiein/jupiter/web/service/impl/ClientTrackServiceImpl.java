package com.qiein.jupiter.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientTrackService;

/**
 * 客资追踪业务层
 * gaoxiaoli
 */
@Service
public class ClientTrackServiceImpl implements ClientTrackService {

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

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchDeleteLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            //TODO 推送
            System.out.println("删除成功");
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
            GoEasyUtil.pushTransfer(staffPO.getCompanyId(), toStaffId, kzIds, newsDao, clientInfoDao);
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
            System.out.println(wrongNum);
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
        if ("100000".equals(jsInfo.getString("code"))) {
            //TODO 推送
            System.out.println("客资回收成功");
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }
}
