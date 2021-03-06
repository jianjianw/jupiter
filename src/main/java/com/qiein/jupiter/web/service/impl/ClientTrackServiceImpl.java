package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.*;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.StaffPushDTO;
import com.qiein.jupiter.web.entity.po.AllotLogPO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.po.ClientStatusPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.StaffNumVO;
import com.qiein.jupiter.web.service.ClientTrackService;
import com.qiein.jupiter.web.service.StaffService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private ClientAllotLogDao clientAllotLogDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private ClientStatusDao clientStatusDao;
    @Autowired
    private StaffService staffService;

    /**
     * 批量删除客资
     *
     * @param kzIds
     * @param staffPO
     */
    public void batchDeleteKzList(String kzIds, StaffPO staffPO, String reason) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzids", kzIds);
        reqContent.put("reason", reason);

        //获取推广人员集合
        //TODO  优化
        List<StaffNumVO> onwerCollector = getOnwerStaffList("COLLECTORID", staffPO.getCompanyId(), kzIds);
        List<StaffNumVO> appoints = getOnwerStaffList("APPOINTORID", staffPO.getCompanyId(), kzIds);

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchDeleteHs");
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
     * 封装推送客资被删除消息
     *
     * @param staffList
     * @param type
     * @param opera
     */
    private void pushRemoveMsg(List<StaffNumVO> staffList, String type, StaffPO opera) {
        if (CollectionUtils.isNotEmpty(staffList)) {
            for (StaffNumVO sf : staffList) {
                if (!sf.isEmpty()) {
                    ClientGoEasyDTO info = null;
                    if (sf.getNum() == 1) {
                        info = clientInfoDao.getClientGoEasyDTOById(sf.getKzId(),
                                DBSplitUtil.getInfoTabName(opera.getCompanyId()),
                                DBSplitUtil.getDetailTabName(opera.getCompanyId()));
                    }
                    GoEasyUtil.pushRemove(opera.getCompanyId(), sf.getStaffId(), info, sf.getNum(), type, opera.getNickName(), newsDao, staffDao);
                }
            }
        }
    }

    /**
     * 获取客资拥有者列表
     *
     * @param type
     * @param companyId
     * @param kzIds
     * @return
     */
    private List<StaffNumVO> getOnwerStaffList(String type, int companyId, String kzIds) {
        if (StringUtil.isEmpty(kzIds)) {
            return null;
        }
        String[] kzIdArr = kzIds.split(CommonConstant.STR_SEPARATOR);
        if (kzIdArr == null || kzIdArr.length == 0) {
            return null;
        }
        return clientDao.getOnwerInfoNumByIds(DBSplitUtil.getInfoTabName(companyId), kzIdArr, " info." + type + " ", companyId);
    }

    /**
     * 批量转移客资
     *
     * @param kzIds
     * @param role
     * @param toStaffId
     * @param staffPO
     */
    public void batchTransferKzList(String kzIds, String role, int toStaffId, StaffPO staffPO, boolean receiveFlag) {
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
        reqContent.put("receiveflag", receiveFlag);

        String addRstStr = crmBaseApi.doService(reqContent, "clientBatchTransferHs");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            //推送给目标员工
            GoEasyUtil.pushTransfer(staffPO, toStaffId, kzIds, newsDao, clientInfoDao, staffDao);
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
    public void approvalInvalidKzList(String kzIds, String memo, int rst, String invalidLabel, StaffPO staffPO) {
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
        if (!"100000".equals(jsInfo.getString("code"))) {
            throw new RException(jsInfo.getString("msg"));
        } else {
            if (rst == ClientStatusConst.BE_INVALID_REJECT || rst == ClientStatusConst.BE_INVALID_ALWAYS) {
                //发送消息给邀约客服
                String[] kzIdArr = kzIds.split(CommonConstant.STR_SEPARATOR);
                for (int i = 0; i < kzIdArr.length; i++) {
                    String kzId = kzIdArr[i];
                    if (StringUtil.isEmpty(kzId)) {
                        continue;
                    }
                    ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzId,
                            DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                            DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
                    if (staffService.getMsgSetByStaffId(info.getCompanyId(), info.getAppointorId()).isAllowWxDingMsg()) {
                        GoEasyUtil.pushReject(staffPO.getCompanyId(), info.getAppointorId(), info, newsDao, staffDao);
                    }
                }
            }
        }
    }

    /**
     * 客资批量分配
     */
    @Override
    public void pushLp(String kzIds, String staffIds, int companyId, int operaId, String operaName) {
        if (StringUtil.isEmpty(kzIds) || StringUtil.isEmpty(staffIds) || NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.ALLOT_ERROR);
        }
//        // 查询所选客资里面没有分出去的客资
//        List<ClientPushDTO> infoList = clientInfoDao.listClientsInStrKzids(kzIds, companyId,
//                DBSplitUtil.getInfoTabName(companyId));
//        if (CollectionUtils.isEmpty(infoList)) {
//            throw new RException(ExceptionEnum.ALLOTED_ERROR);
//        }
        List<String> infoList = Arrays.asList(kzIds.split(CommonConstant.STR_SEPARATOR));
        int kzNum = infoList.size();
        // 查询所选客服集合
        List<StaffPushDTO> staffList = staffDao.listStaffInstrIds(companyId, staffIds, RoleConstant.DSYY);
        if (staffList == null || staffList.size() == 0) {
            throw new RException(ExceptionEnum.APPOINTOR_ERROR);
        }
        while (infoList.size() != 0) {
            for (StaffPushDTO staff : staffList) {
                if (infoList.size() > 0) {
                    // 客资修改最后消息推送时间为当前系统时间，绑定客服，修改状态为分配中
                    int updateRstNum = clientInfoDao.updateClientInfoWhenAllot(companyId,
                            DBSplitUtil.getInfoTabName(companyId), infoList.get(0),
                            ClientStatusConst.KZ_CLASS_NEW, ClientStatusConst.BE_ALLOTING, staff.getStaffId(),
                            staff.getGroupId(), ClientConst.ALLOT_HANDLER);
                    if (1 != updateRstNum) {
                        throw new RException(ExceptionEnum.INFO_STATUS_EDIT_ERROR);
                    }
                    // 客资修改客资的客服组ID，和客服组名称
                    clientInfoDao.updateClientDetailWhenAllot(companyId, DBSplitUtil.getDetailTabName(companyId),
                            infoList.get(0), staff.getStaffName(), staff.getGroupName());
                    staff.doAddKzIdsWill(infoList.get(0));
                    infoList.remove(0);
                } else {
                    break;
                }
            }
        }
        if (kzNum < staffList.size()) {
            for (int i = 0; i < kzNum; i++) {
                push(companyId, staffList.get(i).getWillHaveKzidsStrBf(), staffList.get(i), operaId, operaName);
            }
        } else {
            for (StaffPushDTO staff : staffList) {
                push(companyId, staff.getWillHaveKzidsStrBf(), staff, operaId, operaName);
            }
        }
    }

    public void push(int companyId, String kzIds, StaffPushDTO appoint, int operaId, String operaName) {

        // 根据每个客资生成对应的分配日志
        String[] kzIdsArr = kzIds.split(",");
        String[] allogIdsArr = new String[kzIdsArr.length];
        for (int i = 0; i < kzIdsArr.length; i++) {
            // 生成分配日志
            AllotLogPO allotLog = new AllotLogPO(kzIdsArr[i], appoint.getStaffId(), appoint.getStaffName(),
                    appoint.getGroupId(), appoint.getGroupName(), ClientConst.ALLOT_HANDLER, companyId, operaId,
                    operaName);

            // 记录分配日志
            clientAllotLogDao.addClientAllogLog(DBSplitUtil.getAllotLogTabName(companyId), allotLog);

            allogIdsArr[i] = String.valueOf(allotLog.getId());

            // 客资日志记录
            clientLogDao
                    .addInfoLog(DBSplitUtil.getInfoLogTabName(companyId),
                            new ClientLogPO(
                                    kzIdsArr[i], ClientLogConst.getAllotLog(appoint.getGroupName(),
                                    appoint.getStaffName()),
                                    ClientLogConst.INFO_LOGTYPE_ALLOT, companyId));
        }

        int overTime = companyDao.getById(companyId).getOverTime();
        if (overTime == 0) {
            overTime = CommonConstant.DEFAULT_OVERTIME;
        }

        // 推送消息
        GoEasyUtil.pushAppInfoReceive(companyId, appoint.getStaffId(), kzIdsArr.length, StringUtils.join(kzIdsArr, ","),
                StringUtils.join(allogIdsArr, ","), overTime);
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

    @Override
    public void allotNotArriveShop(String kzId, StaffPO staffPO) {
        //修改客资状态
        List<ClientPushDTO> clientPushDTOS = clientInfoDao.getKzIdExists(kzId, staffPO.getCompanyId(), DBSplitUtil.getInfoTabName(staffPO.getCompanyId()));
        if (CollectionUtils.isEmpty(clientPushDTOS)) {
            throw new RException(ExceptionEnum.KZ_STATUS_ERROR);
        }
        ClientStatusPO clientStatusPO = clientStatusDao.getClientStatusByStatusId(ClientStatusConst.NOT_COME, staffPO.getCompanyId());
        if (null == clientStatusPO) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        clientInfoDao.updateClientInfoStatus(staffPO.getCompanyId(), DBSplitUtil.getInfoTabName(staffPO.getCompanyId()), kzId, clientStatusPO.getClassId(), ClientStatusConst.NOT_COME);
        clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(staffPO.getCompanyId()), new ClientLogPO(kzId, staffPO.getId(), staffPO.getNickName()
                , ClientLogConst.INFO_LOG_NOT_ARRIVE_SHOP, ClientLogConst.INFO_LOGTYPE_EDIT, staffPO.getCompanyId()));
    }
}
