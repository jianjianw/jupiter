package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 付款记录
 * author xiangliang
 */
@RestController
@RequestMapping("/cash")
@Validated
public class CashController extends BaseController {
    @Autowired
    private CashService cashService;

    /**
     * 修改付款记录
     *
     * @param cashLogPO
     * @return
     */
    @PostMapping("/edit_cash")
    public ResultInfo editCash(@RequestBody CashLogPO cashLogPO) {
        StaffPO staff = getCurrentLoginStaff();
        cashLogPO.setCompanyId(staff.getCompanyId());
        cashLogPO.setOperaId(staff.getId());
        cashLogPO.setOperaName(staff.getNickName());
        cashService.editCash(cashLogPO);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 添加收款记录
     *
     * @return
     */
    @PostMapping("/add_cash_log")
    public ResultInfo addCashLog(@Validated @RequestBody CashLogPO cashLogPO) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        cashLogPO.setCompanyId(currentLoginStaff.getCompanyId());
        cashLogPO.setOperaId(currentLoginStaff.getId());
        cashLogPO.setOperaName(currentLoginStaff.getNickName());
        cashService.addCashLog(cashLogPO);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 收款记录查询页面
     * @param kzId
     * @return
     */
    @GetMapping("/find_cash_log")
    public ResultInfo findCashLog(@RequestParam String kzId){
        return  ResultInfoUtil.success(cashService.findCashLog(kzId, DBSplitUtil.getTable(TableEnum.cash_log,getCurrentLoginStaff().getCompanyId())));
    }
}
