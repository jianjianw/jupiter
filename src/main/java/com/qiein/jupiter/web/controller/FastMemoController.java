package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.BrandPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.BrandService;
import com.qiein.jupiter.web.service.FastMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 快捷备注
 *
 * @author gaoxiaoli 2018/8/11
 */
@RestController
@RequestMapping("/memo")
@Validated
public class FastMemoController extends BaseController {

    @Autowired
    private FastMemoService fastMemoService;

    /**
     * 获取快捷备注列表
     *
     * @return
     */
    @GetMapping("/get_fast_memo_list")
    public ResultInfo getFastMemoList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(fastMemoService.getMemoListById(currentLoginStaff.getId(), currentLoginStaff.getCompanyId()));
    }

    /**
     * 新增快捷备注
     *
     * @return
     */
    @GetMapping("/add_fast_memo")
    public ResultInfo addFastMemo(@NotEmptyStr String memo) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        fastMemoService.addFastMemo(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), memo);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 删除快捷备注
     *
     * @return
     */
    @GetMapping("/delete_fast_memo")
    public ResultInfo deleteFastMemo(@Id int id) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        fastMemoService.deleteFastMemo(id);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }
}
