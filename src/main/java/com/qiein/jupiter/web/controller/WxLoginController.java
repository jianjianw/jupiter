package com.qiein.jupiter.web.controller;

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
@RequestMapping("/wx_login")
public class WxLoginController extends BaseController {

    @GetMapping("/get_code")
    public void getCode(@RequestParam String code) {
        System.out.println("微信code:" + code);
    }

}
