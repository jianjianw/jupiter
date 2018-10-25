package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.ClientSourceDTO;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ClientEditService;

/**
 * 客资编辑
 */
@RestController
@RequestMapping("/edit")
public class ClientEditController extends BaseController {

    @Autowired
    private ClientEditService clientEditService;

    @Autowired
    private SystemLogService logService;

    /**
     * 推广编辑客资
     *
     * @return
     */
    @PostMapping("/edit_client_by_dscj")
    public ResultInfo editClientByDscj(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(), clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        if (StringUtil.isEmpty(clientVO.getKzId())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByDscj(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }


    /**
     * 邀约编辑客资
     *
     * @return
     */
    @PostMapping("/edit_client_by_dsyy")
    public ResultInfo editClientByDsyy(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(), clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        if (StringUtil.isEmpty(clientVO.getKzId())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //预约到店
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.BE_COMFIRM == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getShopId())) {
            return ResultInfoUtil.error(ExceptionEnum.SHOP_ID_IS_NULL);
        }
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.BE_COMFIRM == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getAppointTime())) {
            return ResultInfoUtil.error(ExceptionEnum.APPOINT_TIME_IS_NULL);
        }
        // 在线订单
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getAmount())) {
            return ResultInfoUtil.error(ExceptionEnum.AMOUNT_IS_NULL);
        }
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getSuccessTime())) {
            return ResultInfoUtil.error(ExceptionEnum.SUCCESS_TIME_IS_NULL);
        }
        //在线保留
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_STAY == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getPayAmount())) {
            return ResultInfoUtil.error(ExceptionEnum.STAY_AMOUNT_IS_NULL);
        }
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_STAY == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getPayTime())) {
            return ResultInfoUtil.error(ExceptionEnum.STAY_TIME_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByDsyy(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }


    /**
     * 门市编辑客资
     *
     * @return
     */
    @PostMapping("/edit_client_by_msjd")
    public ResultInfo editClientByMsjd(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(), clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        if (StringUtil.isEmpty(clientVO.getKzId())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByMsjd(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }


    /**
     * 主管纠错
     *
     * @return
     */
    @PostMapping("/edit_client_by_cwzx")
    public ResultInfo editClientByCwzx(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(), clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        if (StringUtil.isEmpty(clientVO.getKzId())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByCwzx(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 修改客资详情
     *
     * @return
     */
    @PostMapping("/edit_client_detail")
    public ResultInfo editClientDetail(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(), clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        if (StringUtil.isEmpty(clientVO.getKzId())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientDetail(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 快捷备注
     *
     * @return
     */
    @GetMapping("/edit_fast_memo")
    public ResultInfo editFastMemo(@NotEmptyStr String kzId, @NotEmptyStr String memo) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editFastMemo(kzId, memo, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 渠道转移客资
     *
     * @param clientSourceDTO
     */
    @PostMapping("/change_client_source")
    public ResultInfo changeClientSource(@RequestBody ClientSourceDTO clientSourceDTO) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientSourceDTO.setCompanyId(currentLoginStaff.getCompanyId());
        clientEditService.changeClientSource(clientSourceDTO);
        try {
            // 日志记录
            RequestInfoDTO requestInfo = getRequestInfo();
            logService.addLog(new SystemLog(SysLogUtil.LOG_TYPE_CLIENT, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), currentLoginStaff.getNickName()+"将 "+clientSourceDTO.getOldSrcName()+" 客资转移到了 "+clientSourceDTO.getNewSrcName(),
                    currentLoginStaff.getCompanyId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.TRANSFER_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.TRANSFER_SUCCESS);
    }

}
