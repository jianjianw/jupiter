package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.DBSplitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.service.StatusService;

@RestController
@RequestMapping("/status")
@Validated
public class StatusController extends BaseController {

    @Autowired
    private StatusService statusService;

    /**
     * 获取企业状态信息
     *
     * @return
     */
    @GetMapping("/get_company_status_list")
    public ResultInfo getCompanyStatusList() {
        return ResultInfoUtil.success(statusService.getCompanyStatusList(getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 修改状态
     *
     * @param statusPO
     * @return
     */
    @PostMapping("/edit_status")
    public ResultInfo editStatus(@RequestBody StatusPO statusPO) {
        if (NumUtil.isInValid(statusPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.ID_IS_NULL);
        }
        // 获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        statusPO.setCompanyId(currentLoginStaff.getCompanyId());
        statusService.editStatus(statusPO);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 修改状态前景色，背景色为默认颜色
     *
     * @param statusPO
     * @return
     */
    @PostMapping("edit_color_to_default")
    public ResultInfo editColorToDefault(@RequestBody StatusPO statusPO) {
        if (NumUtil.isInValid(statusPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.ID_IS_NULL);
        }
        if (!StatusPO.STS_BGCOLOR.equals(statusPO.getColumn())
                && !StatusPO.STS_FONTCOLOR.equals(statusPO.getColumn())) {
            return ResultInfoUtil.error(ExceptionEnum.STS_COLUMN_ERROR);
        }
        statusService.editColorToDefault(getCurrentLoginStaff().getCompanyId(), statusPO.getId(), statusPO.getColumn());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }
    /**
     * 修改客资状态
     */
    @GetMapping("/edit_client_status")
    public ResultInfo editClientStatus(@RequestParam boolean showFlag, @RequestParam int id){
        statusService.editClientStatus(showFlag,id);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }
    /**
     * 修改客资状态
     */
    @GetMapping("/edit_kzphone_flag")
    public ResultInfo editKzphoneFlag(@RequestParam String kzId,@RequestParam int kzphoneFlag){
        statusService.editKzphoneFlag(kzId,kzphoneFlag, DBSplitUtil.getTable(TableEnum.info,getCurrentLoginStaff().getCompanyId()));
        return  ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }
}