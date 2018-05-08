package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.MobileLocationUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/outer")
public class OuterController {

    @GetMapping("/get_address_by_phone")
    public ResultInfo getAddressByPhone(@NotEmptyStr @RequestParam("phone") String phone) {
        return ResultInfoUtil.success(TipMsgConstant.SUCCESS, MobileLocationUtil.getPhoneLocation(phone));
    }
}
