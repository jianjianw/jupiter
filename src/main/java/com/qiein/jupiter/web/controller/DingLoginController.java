package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.web.service.DingLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉登录相关
 *
 * @Author: shiTao
 */
@RestController
@RequestMapping("/ding_login")
public class DingLoginController {

    @Autowired
    private DingLoginService dingLoginService;

    @GetMapping("/post_code")
    public void postCode(String code) {
//        dingLoginService.getPersistentCode(code);
    }
}
