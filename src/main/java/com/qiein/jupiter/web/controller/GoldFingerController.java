package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.GoldFingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@RestController
@RequestMapping("/gold_finger")
@Validated
public class GoldFingerController extends BaseController{
    @Autowired
    private GoldFingerService goldFingerService;

    /**
     * 增加金数据表单
     * @param goldFingerPO
     * @return
     */
    @PostMapping("/insert")
    public ResultInfo insert(@RequestBody GoldFingerPO goldFingerPO){
        StaffPO staff=getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        goldFingerService.insert(goldFingerPO);
        return ResultInfoUtil.success();
    }

    /**
     * 删除金数据表单
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam Integer id){
        goldFingerService.delete(id);
        return ResultInfoUtil.success();
    }

    /**
     * 更新金数据表单
     * @param goldFingerPO
     * @return
     */
    @PostMapping("/update")
    public ResultInfo update(@RequestBody GoldFingerPO goldFingerPO){
        StaffPO staff=getCurrentLoginStaff();
        goldFingerPO.setStaffId(staff.getId());
        goldFingerPO.setCompanyId(staff.getCompanyId());
        goldFingerService.update(goldFingerPO);
        return ResultInfoUtil.success();
    }

    /**
     * 金数据表单页面显示
     */
    @GetMapping("/select")
    public ResultInfo select(){
        StaffPO staff=getCurrentLoginStaff();
        List<GoldFingerPO> list =goldFingerService.select(staff.getCompanyId());
        return ResultInfoUtil.success(list);
    }
}
