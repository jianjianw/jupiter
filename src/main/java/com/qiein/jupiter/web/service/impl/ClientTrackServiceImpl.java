package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
            //TODO 推送
            System.out.println("转移成功");
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }
}
