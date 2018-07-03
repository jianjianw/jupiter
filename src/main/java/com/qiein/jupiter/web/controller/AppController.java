package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientReceiveService;

/**
 * 客户端
 *
 * @author JingChenglong 2018/05/09 15:51
 */
@RestController
@RequestMapping("/app")
@Validated
public class AppController extends BaseController {

    @Autowired
    private ClientReceiveService receiveService;

    @Autowired
    private StaffService staffService;

    /**
     * 客资领取
     *
     * @param kzId
     * @param logId
     * @return
     */
    @GetMapping("/receive")
    public ResultInfo reveice(@RequestParam String kzId, @RequestParam String logId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        if (StringUtil.haveEmpty(kzId, logId)) {
            ResultInfoUtil.error(ExceptionEnum.INFO_ERROR);
        }
        // 客资领取
        receiveService.receive(kzId, logId, currentLoginStaff.getCompanyId(), currentLoginStaff.getId(),
                currentLoginStaff.getNickName());
        return ResultInfoUtil.success(TipMsgEnum.INFO_RECEIVE_SUCCESS);
    }

    /**
     * 客资领取
     *
     * @param kzId
     * @param logId
     * @return
     */
    @GetMapping("/wx_receive")
    public ResultInfo reveice(@RequestParam String kzId, @RequestParam String logId, @RequestParam Integer staffId, @RequestParam Integer companyId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = staffService.getById(staffId, companyId);
        if (StringUtil.haveEmpty(kzId, logId)) {
            ResultInfoUtil.error(ExceptionEnum.INFO_ERROR);
        }
        // 客资领取
        receiveService.receive(kzId, logId, currentLoginStaff.getCompanyId(), currentLoginStaff.getId(),
                currentLoginStaff.getNickName());
        return ResultInfoUtil.success(TipMsgEnum.INFO_RECEIVE_SUCCESS);
    }

    /**
     * 客资领取
     *
     * @param kzId
     * @param logId
     * @return
     */
    @GetMapping("/pc_receive")
    public ResultInfo pcReveice(@RequestParam String kzId, @RequestParam String logId, @RequestParam Integer staffId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        if (StringUtil.haveEmpty(kzId, logId)) {
            ResultInfoUtil.error(ExceptionEnum.INFO_ERROR);
        }
        // 客资领取
        receiveService.receive(kzId, logId, currentLoginStaff.getCompanyId(), currentLoginStaff.getId(),
                currentLoginStaff.getNickName());
        return ResultInfoUtil.success(TipMsgEnum.INFO_RECEIVE_SUCCESS);
    }


    /**
     * 客资拒接
     *
     * @param kzId
     * @param logId
     * @return
     */
    @GetMapping("/refuse")
    public ResultInfo refuse(@RequestParam String kzId, @RequestParam String logId) {
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        if (StringUtil.haveEmpty(kzId, logId)) {
            ResultInfoUtil.error(ExceptionEnum.INFO_ERROR);
        }
        // 客资拒接
        receiveService.refuse(kzId, logId, currentLoginStaff.getCompanyId(), currentLoginStaff.getId(),
                currentLoginStaff.getNickName());
        return ResultInfoUtil.success(TipMsgEnum.INFO_REFUSE_SUCCESS);
    }
}