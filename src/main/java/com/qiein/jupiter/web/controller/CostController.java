package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TipMsgEnum;
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
    @GetMapping("cost_list")
    public ResultInfo costList(@RequestParam String month) {
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(costService.costList(month, staff.getCompanyId(),staff.getId()));
    }

    /**
     * 花费修改
     */
    @PostMapping("edit_cost")
    public ResultInfo editCost(@RequestBody CostPO costPO) {
        /**StaffPO staff = getCurrentLoginStaff();
        costPO.setCompanyId(staff.getCompanyId());
        if (StringUtil.haveEmpty(costPO.getId())) {
            int id = costService.insert(costPO);
            addCostLog(staff, costPO.getId(), "新增" + costPO.getCostTime() + "花费：" + costPO.getCost());
        } else {
            costService.editCost(costPO);
            addCostLog(staff, costPO.getId(), "修改" + costPO.getCostTime() + "花费金额为：" + costPO.getCost());
        }**/
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 添加花费记录
     *
     * @param staff
     * @param id
     * @param memo
     */
    private void addCostLog(StaffPO staff, Integer id, String memo) {
        CostLogPO costLog = new CostLogPO();
        costLog.setCompanyId(staff.getCompanyId());
        costLog.setCostId(id);
        costLog.setOperaId(staff.getId());
        costLog.setOperaName(staff.getNickName());
        costLog.setMemo(memo);
        costService.createCostLog(costLog);
    }
}
