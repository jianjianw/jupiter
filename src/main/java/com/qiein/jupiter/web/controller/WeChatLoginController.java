package com.qiein.jupiter.web.controller;

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

    @GetMapping("/get_code")
    public void getCode(@RequestParam String code) {
        weChatLoginService.getAccessToken(code);
    }

}
