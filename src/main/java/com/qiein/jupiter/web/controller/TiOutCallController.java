package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.OutCallUserDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.OutCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 天润外呼系统
 *
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
    public JSONObject online(String phone) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.userOnlineOffline(staff.getCompanyId(), staff.getId(), phone, true);
    }

    /**
     * 下线
     *
     * @return
     */
    @GetMapping("/offline")
    public JSONObject offline() {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.userOnlineOffline(staff.getCompanyId(), staff.getId(), null, false);
    }


    /**
     * 增加外呼账户
     */
    @GetMapping("/add_user")
    public JSONObject addUser(int staffId, String bindPhone, String cno) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.createCallAccount(staff.getCompanyId(), staffId, bindPhone, cno);
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
    public JSONObject callPhone(String phone, String kzId) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.callPhone(staff.getCompanyId(), staff.getId(), phone, kzId);
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


    /**
     * 挂断
     */
    @GetMapping("/hangup")
    public JSONObject hangup() {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.hangupPhone(staff.getCompanyId(), staff.getId());
    }


    /**
     * 查询通话 记录
     */
    @GetMapping("/record")
    public JSONObject record(String kzId) {
        StaffPO staff = getCurrentLoginStaff();
        return outCallService.getCallRecord(staff.getCompanyId(), kzId);
    }

    /**
     * 修改企业管理员信息
     */
    @PostMapping("/update_admin")
    public ResultInfo updateAdmin(@RequestBody OutCallUserDTO outCallUserDTO) {
        ObjectUtil.objectStrParamTrim(outCallUserDTO);
        StaffPO staff = getCurrentLoginStaff();
        outCallUserDTO.setCompanyId(staff.getCompanyId());
        outCallService.updateAdmin(outCallUserDTO);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 获取企业管理员账户
     *
     * @return
     */
    @GetMapping("/get_admin_user")
    public ResultInfo getAdminUser() {
        return ResultInfoUtil.success(outCallService.getAdminByCompanyId(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 获取个人账户信息
     */
    @GetMapping("/get_user_info")
    public ResultInfo getUserInfo() {
        StaffPO staff = getCurrentLoginStaff();
        OutCallUserDTO userInfo = outCallService.getUserInfo(staff.getCompanyId(), staff.getId());
        return ResultInfoUtil.success(userInfo);
    }

    /**
     * 批量新增
     *
     * @param staffIds
     * @return
     */
    @GetMapping("/batch_add_user")
    public ResultInfo batchAddUser(String staffIds) {
        StaffPO staff = getCurrentLoginStaff();
        outCallService.batchAddUser(staffIds, staff.getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }


    /**
     * 获取绑定员工列表
     *
     * @return
     */
    @GetMapping("/get_user_list")
    public ResultInfo getUserList() {
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(outCallService.getUserList(staff.getCompanyId()));
    }
}
