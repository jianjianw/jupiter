package com.qiein.jupiter.web.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 字典数据
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/get_invalid_reason_list")
    public ResultInfo getInvalidReasonList() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(dictionaryService.getDicByType(currentLoginStaff.getCompanyId(), DictionaryConstant.INVALID_REASON));
    }

    @PostMapping("/add_invalid_reason")
    public ResultInfo addInvalidReason(@RequestBody DictionaryPO dictionaryPO) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.addInvalidReason(dictionaryPO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }
}
