package com.qiein.jupiter.web.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典数据
 */
@RestController
@RequestMapping("/dictionary")
@Validated
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
        if (StringUtil.isEmpty(dictionaryPO.getSpare())) {
            return ResultInfoUtil.error(ExceptionEnum.INVALID_REASON_TYPE_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.addInvalidReason(dictionaryPO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    @PostMapping("/edit_invalid_reason")
    public ResultInfo editInvalidReason(@RequestBody DictionaryPO dictionaryPO) {
        if (StringUtil.isEmpty(dictionaryPO.getSpare())) {
            return ResultInfoUtil.error(ExceptionEnum.INVALID_REASON_TYPE_NULL);
        }
        if (NumUtil.isNull(dictionaryPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.ID_IS_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryPO.setCompanyId(currentLoginStaff.getCompanyId());
        dictionaryService.editInvalidReason(dictionaryPO);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SUCCESS);
    }

    @GetMapping("/delete_invalid_reason")
    public ResultInfo deleteInvalidReason(@NotEmptyStr @RequestParam("ids") String ids) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        dictionaryService.batchDeleteByIds(currentLoginStaff.getCompanyId(), ids);
        return ResultInfoUtil.success(TipMsgConstant.DELETE_SUCCESS);
    }
}
