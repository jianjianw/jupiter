package com.qiein.jupiter.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TigMsgEnum;
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
        if (StringUtil.isEmpty(clientVO.getKzId())){
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByDscj(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        if (StringUtil.isEmpty(clientVO.getKzId())){
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByDsyy(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        if (StringUtil.isEmpty(clientVO.getKzId())){
            return ResultInfoUtil.error(ExceptionEnum.KZ_ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientEditService.editClientByCwzx(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }


}
