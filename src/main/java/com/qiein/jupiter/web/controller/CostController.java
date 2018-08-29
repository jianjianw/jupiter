package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.CostLogPO;
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
public class CostController extends BaseController {
    @Autowired
    private CostService costService;

    /**
     * 获取花费页面信息
     */
    @GetMapping("/cost_list")
    public ResultInfo costList(@RequestParam String month) {
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(costService.costList(month, staff.getCompanyId(),staff.getId()));
    }
    /**
     * 修改返利率
     */
    @GetMapping("/edit_rate")
    public ResultInfo editRate(@RequestParam String srcIds, @RequestParam Integer start, @RequestParam Integer end, @RequestParam BigDecimal rate){
        costService.editRate(srcIds,start,end,rate,getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }
    /**
     * 花费修改
     */
    @PostMapping("/edit_cost")
    public ResultInfo editCost(@RequestBody CostPO costPO) {
        if(costPO.getSrcId()==0){
            throw new RException(ExceptionEnum.CANT_EDIT_HJ);
        }
        costPO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        StaffPO staff= getCurrentLoginStaff();
        CostLogPO costLog = new CostLogPO();
        costLog.setCompanyId(staff.getCompanyId());
        costLog.setOperaId(staff.getId());
        costLog.setOperaName(staff.getNickName());
        costService.editCost(costPO,costLog);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }


}
