package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.RoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientPushService;
import com.qiein.jupiter.web.service.ClientTrackService;

/**
 * 客资录入
 */
@RestController
@RequestMapping("/track")
public class ClientTrackController extends BaseController {

    @Autowired
    private ClientTrackService clientTrackService;
    @Autowired
    private ClientPushService clientPushService;

    /**
     * 批量删除客资
     *
     * @return
     */
    @PostMapping("/batch_delete_kz_list")
    public ResultInfo batchDeleteKzList(@RequestBody JSONObject jsonObject) {
        String kzIds = StringUtil.nullToStrTrim(jsonObject.getString("kzIds"));
        if (StringUtil.isEmpty(kzIds)) {
            throw new RException(ExceptionEnum.KZ_ID_IS_NULL);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientTrackService.batchDeleteKzList(kzIds, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 批量转移客资
     *
     * @return
     */
    @PostMapping("/batch_transfer_kz_list")
    public ResultInfo batchTransferKzList(@RequestBody JSONObject jsonObject) {
        String kzIds = StringUtil.nullToStrTrim(jsonObject.getString("kzIds"));
        if (StringUtil.isEmpty(kzIds)) {
            throw new RException(ExceptionEnum.KZ_ID_IS_NULL);
        }
        int toStaffId = jsonObject.getIntValue("toStaffId");
        if (NumUtil.isNull(toStaffId)) {
            throw new RException(ExceptionEnum.STAFF_ID_NULL);
        }
        String role = StringUtil.nullToStrTrim(jsonObject.getString("role"));
        if (StringUtil.isEmpty(role)) {
            throw new RException(ExceptionEnum.INVALID_REASON_TYPE_NULL);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientTrackService.batchTransferKzList(kzIds, role, toStaffId, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.TRANSFER_SUCCESS);
    }

    /**
     * 无效审批
     *
     * @return
     */
    @PostMapping("/approval_invalid_kz_list")
    public ResultInfo approvalInvalidKzList(@RequestBody JSONObject jsonObject) {
        String kzIds = StringUtil.nullToStrTrim(jsonObject.getString("kzIds"));
        if (StringUtil.isEmpty(kzIds)) {
            throw new RException(ExceptionEnum.KZ_ID_IS_NULL);
        }
        String memo = StringUtil.nullToStrTrim(jsonObject.getString("memo"));
        int rst = jsonObject.getIntValue("rst");
        if (NumUtil.isNull(rst)) {
            throw new RException(ExceptionEnum.APPROVAL_RST_IS_NULL);
        }
        if (ClientStatusConst.BE_INVALID_REJECT == rst && StringUtil.isEmpty(memo)) {
            throw new RException(ExceptionEnum.APPROVAL_MEMO_IS_NULL);
        }
        String invalidLabel = StringUtil.nullToStrTrim(jsonObject.getString("invalidLabel"));
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientTrackService.approvalInvalidKzList(kzIds, memo, rst, invalidLabel, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.APPROVAL_SUCCESS);
    }

    /**
     * 分配
     *
     * @return
     */
    @PostMapping("/allot")
    public ResultInfo allot(@RequestBody JSONObject jsonObject) {
        String kzIds = StringUtil.nullToStrTrim(jsonObject.getString("kzIds"));
        if (StringUtil.isEmpty(kzIds)) {
            throw new RException(ExceptionEnum.KZ_ID_IS_NULL);
        }
        String staffIds = StringUtil.nullToStrTrim(jsonObject.getString("staffIds"));
        if (StringUtil.isEmpty(staffIds)) {
            throw new RException(ExceptionEnum.STAFF_ID_NULL);
        }
        String role = StringUtil.nullToStrTrim(jsonObject.getString("role"));
        if (StringUtil.isEmpty(role)) {
            throw new RException(ExceptionEnum.ROLE_ERROR);
        }
        StaffPO currentStaff = getCurrentLoginStaff();
        //分配给客服
        if (RoleConstant.DSCJ.equals(role) || RoleConstant.DSSX.equals(role) || RoleConstant.ZJSSX.equals(role)) {
            clientPushService.pushLp(kzIds, staffIds, currentStaff.getCompanyId(), currentStaff.getId(),
                    currentStaff.getNickName(), role.startsWith("ds") ? RoleConstant.DSYY : RoleConstant.ZJSYY);
        } else if (RoleConstant.DSYY.equals(role) || RoleConstant.ZJSYY.equals(role) ||
                RoleConstant.MSJD.equals(role) || RoleConstant.CWZX.equals(role)) {
            //分配给门市
            clientPushService.allotToMsjd(kzIds, staffIds, currentStaff.getCompanyId(), currentStaff.getId(),
                    currentStaff.getNickName());
        }
        return ResultInfoUtil.success(TipMsgEnum.ALLOT_SUCCESS);
    }

    /**
     * 批量恢复，回收客资
     *
     * @return
     */
    @PostMapping("/batch_restore_kz_list")
    public ResultInfo batchRestoreKzList(@RequestBody JSONObject jsonObject) {
        String kzIds = StringUtil.nullToStrTrim(jsonObject.getString("kzIds"));
        if (StringUtil.isEmpty(kzIds)) {
            throw new RException(ExceptionEnum.KZ_ID_IS_NULL);
        }
        clientTrackService.batchRestoreKzList(kzIds, getCurrentLoginStaff());
        return ResultInfoUtil.success(TipMsgEnum.RESTORE_SUCCESS);
    }
}