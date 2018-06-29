package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.NumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
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
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getStayAmount())) {
            return ResultInfoUtil.error(ExceptionEnum.STAY_AMOUNT_IS_NULL);
        }
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getSuccessTime())) {
            return ResultInfoUtil.error(ExceptionEnum.SUCCESS_TIME_IS_NULL);
        }
        //在线保留
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_STAY == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getStayAmount())) {
            return ResultInfoUtil.error(ExceptionEnum.STAY_AMOUNT_IS_NULL);
        }
        if (NumUtil.isValid(clientVO.getYyRst()) && ClientStatusConst.ONLINE_STAY == clientVO.getYyRst() && NumUtil.isInValid(clientVO.getStayTime())) {
            return ResultInfoUtil.error(ExceptionEnum.STAY_TIME_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByDsyy(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }


    /**
     * 邀约编辑客资
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


}
