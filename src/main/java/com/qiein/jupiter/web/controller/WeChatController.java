package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.wechat.WeChatPushUtil;
import com.qiein.jupiter.web.entity.dto.WeChatUserDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @create by Tt(叶华葳) 2018-06-06 11:47
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController extends BaseController {

    @Resource
    private StaffService staffService;

    @GetMapping("/get_qr_code_img")
    public ResultInfo getQRCode(){
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(WeChatPushUtil.getQRCodeImg(staffPO.getId(),staffPO.getCompanyId()));
    }

    @GetMapping("/remove_bind")
    public ResultInfo removeBind(){
        StaffPO staffPO = getCurrentLoginStaff();
        WeChatPushUtil.removeBind(staffPO.getCompanyId(), staffPO.getId());
        return ResultInfoUtil.success();
    }

    @GetMapping("/check_bind")
    public ResultInfo checkWXBind(){
        WeChatUserDTO weChatUserDTO = staffService.checkWXBind(getCurrentLoginStaff().getCompanyId(),getCurrentLoginStaff().getId());
        return ResultInfoUtil.success(weChatUserDTO);
    }

    /**
     * 获取员工今日信息
     * @return
     */
    @GetMapping("/get_staff_info")
    public ResultInfo getStaffInfo(int companyId,int staffId){
        staffService.getWXStaffInfo(companyId,staffId);
        return ResultInfoUtil.success();
    }
}
