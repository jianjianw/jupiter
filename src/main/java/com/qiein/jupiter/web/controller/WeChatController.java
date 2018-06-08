package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.wechat.WeChatPushUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @create by Tt(叶华葳) 2018-06-06 11:47
 */
@RestController
@RequestMapping("/wechat")
public class WeChatController extends BaseController {

    private static final String testAppId = "wxcfb9db7577fca934";
    private static final String testAppSecret = "c00e1dc5cf3c7c305ccf9e0b9dd6158e";

//    @GetMapping("/auth")
//    public ResultInfo weChatAuth(String signature,
//                                 String timestamp ,
//                                 Integer nonce ,
//                                 String echostr){
//        System.out.println("signature: "+signature);
//        System.out.println("timestamp: "+timestamp);
//        System.out.println("nonce: "+nonce);
//        System.out.println("echostr: "+echostr);
//        System.out.println("token: "+ WeChatPushUtil.getAccessToken());
//        return ResultInfoUtil.success();
//    }

    @GetMapping("/get_qr_code_img")
    public ResultInfo getQRCode(){
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(WeChatPushUtil.getQRCodeImg(staffPO.getId(),staffPO.getCompanyId()));
    }

}
