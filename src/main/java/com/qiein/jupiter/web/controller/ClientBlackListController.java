package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.RegexUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.BlackListPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ClientBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 黑名单
 * @author: xiangliang
 */
@RestController
@RequestMapping("/black_list")
public class ClientBlackListController extends BaseController{
    @Autowired
    private ClientBlackListService clientBlackListService;

    /**
     * 删除黑名单
     * @param ids
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam String ids){
        clientBlackListService.delete(ids);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 查找
     * @return
     */
    @GetMapping("/select")
    public ResultInfo select(){
        return ResultInfoUtil.success(clientBlackListService.select(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 添加黑名单
     * @param blackListPO
     * @return
     */
    @PostMapping("/insert")
    public ResultInfo insert(@RequestBody BlackListPO blackListPO){
        StaffPO staff=getCurrentLoginStaff();
        blackListPO.setCompanyId(staff.getCompanyId());
        blackListPO.setStaffId(staff.getId());
        blackListPO.setStaffName(staff.getNickName());
        clientBlackListService.insert(blackListPO);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }
    /**
     * 修改黑名单
     */
    @PostMapping("/update")
    public ResultInfo update(@RequestBody BlackListPO blackListPO){
        clientBlackListService.update(blackListPO);
        return ResultInfoUtil.success(TipMsgEnum.UPDATE_SUCCESS);
    }
}
