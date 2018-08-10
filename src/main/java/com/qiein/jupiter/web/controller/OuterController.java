package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.MobileLocationUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.service.ClientAddService;
import com.qiein.jupiter.web.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 开放端口 不需要验证
 */
@RestController
@RequestMapping("/outer")
public class OuterController {

    @Autowired
    private ClientAddService clientAddService;
    @Autowired
    private ClientService clientService;

    /**
     * 根据手机号获取地址
     *
     * @param phone
     * @return
     */
    @GetMapping("/get_address_by_phone")
    public ResultInfo getAddressByPhone(@NotEmptyStr @RequestParam("phone") String phone) {
        return ResultInfoUtil.success(TipMsgEnum.SUCCESS, MobileLocationUtil.getPhoneLocation(phone));
    }

    /**
     * 解析批量录入字符串，转换成json
     *
     * @param jsonObject
     * @return
     */

    @PostMapping("/change_str_to_info")
    public ResultInfo changeStrToInfo(@RequestBody JSONObject jsonObject) {
        String text = StringUtil.nullToStrTrim(jsonObject.getString("text"));
        if (StringUtil.isEmpty(text)) {
            throw new RException(ExceptionEnum.BATCH_ADD_NULL);
        }
        return ResultInfoUtil.success(clientAddService.changeStrToInfo(text));
    }

    /**
     * 扫描微信二维码
     *
     * @param kzId
     * @return
     */

    @GetMapping("/scan_wechat")
    public ResultInfo scanWechat(@Id int companyId, @NotEmptyStr String kzId) {
        clientService.scanWechat(companyId, kzId);
        return ResultInfoUtil.success();
    }

    @GetMapping("/get_uuid")
    public ResultInfo getUUId() {
        String uuid = UUID.randomUUID().toString().replace("-","" );
        return ResultInfoUtil.success(uuid);
    }


}
