package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.web.entity.po.StaffDetailPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import com.qiein.jupiter.web.service.WeChatLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信登录相关
 *
 * @Author: shiTao
 */
@RestController
@RequestMapping("/wechat_login")
public class WeChatLoginController extends BaseController {

    @Autowired
    private WeChatLoginService weChatLoginService;

    @Autowired
    private StaffService staffService;
    
    @GetMapping("/get_code_for_login")
    public void getCodeForLogin(@RequestParam String code) {
        weChatLoginService.getAccessToken(code);
    }

    @GetMapping("get_code_for_save")
    public void getCodeForSave(@RequestParam String code){
    	StaffDetailPO staffDetailPO=weChatLoginService.getAccessToken(code);
    	StaffPO staff=getCurrentLoginStaff();
    	staffDetailPO.setId(staff.getId());
    	staffService.saveWechat(staffDetailPO);
    }
}
