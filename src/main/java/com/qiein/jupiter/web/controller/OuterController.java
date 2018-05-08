package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.MobileLocationUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outer")
public class OuterController {

    @GetMapping("/get_address_by_phone")
    public ResultInfo getAddressByPhone(@NotEmptyStr @RequestParam("phone") String phone) {
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS, MobileLocationUtil.getPhoneLocation(phone));
    }
}
