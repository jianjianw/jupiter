package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.dto.OutCallUserDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.OutCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shiTao
 */
@RestController
@RequestMapping("/ti_out_call")
public class TiOutCallController extends BaseController {

    @Autowired
    private OutCallService outCallService;

    /**
     * 上线
     *
     * @return
     */
    @GetMapping("/online")
    public JSONObject online() {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.userOnlineOffline(staff.getCompanyId(), staff.getId(), true);
    }

    /**
     * 下线
     *
     * @return
     */
    @GetMapping("/offline")
    public JSONObject offline() {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.userOnlineOffline(staff.getCompanyId(), staff.getId(), false);
    }


    /**
     * 增加外呼账户
     */
    @GetMapping("/add_user")
    public JSONObject addUser(int staffId, String bindPhone) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.createCallAccount(staff.getCompanyId(), staffId, bindPhone);
    }

    /**
     * 删除外呼账户
     */
    @GetMapping("/del_user")
    public JSONObject delUser(int staffId) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.delCallAccount(staff.getCompanyId(), staffId);
    }

    /**
     * 修改绑定手机号
     *
     * @param phone
     * @return
     */
    @GetMapping("/change_bind_tel")
    public JSONObject changeBindTel(int staffId, String phone) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.updateCallAccountPhone(staff.getCompanyId(), staffId, phone);
    }


    /**
     * 拨打电话
     */
    @GetMapping("/call")
    public JSONObject callPhone(String phone) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.callPhone(staff.getCompanyId(), staff.getId(), phone);
    }

    /**
     * 获取验证码
     */
    @GetMapping("/get_validate_code")
    public JSONObject getValidateCode(int staffId, String phone) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.getValidateCode(staff.getCompanyId(), staffId, phone);
    }

    /**
     * 添加手机号码到白名单
     */
    @GetMapping("/add_to_white")
    public JSONObject addToWhite(String phone, String key, String code) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.addToWhite(staff.getCompanyId(), staff.getId(), phone, key, code);
    }

}
