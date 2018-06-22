package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.CostPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.CostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 电商渠道花费
 * Author xiangliang 2018/6/22
 */
@RestController
@RequestMapping("/cost")
@Validated
public class CostController extends BaseController{
    @Autowired
    private CostService costService;

    /**
     * 获取花费页面信息
     */
    @GetMapping("cost_list")
    public ResultInfo costList(@RequestParam String month){
        StaffPO staff=getCurrentLoginStaff();
        return ResultInfoUtil.success(costService.costList(month,staff.getCompanyId()));
    }
    /**
     * 花费修改
     */
    @PostMapping("edit_cost")
    public ResultInfo editCost(@RequestBody CostPO costPO){
        StaffPO staff=getCurrentLoginStaff();
        costPO.setCompanyId(staff.getCompanyId());
        if(StringUtil.haveEmpty(costPO.getId())){
            costService.insert(costPO);
        }else{
            costService.editCost(costPO);
        }
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }
}
