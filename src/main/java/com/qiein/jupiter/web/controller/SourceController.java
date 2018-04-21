package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.annotation.NotEmpty;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 来源Controller
 */
@RestController
@RequestMapping("/source")
public class SourceController extends BaseController{

    @Autowired
    private SourceService sourceService;

    /**
     *  新增来源
     * @param sourcePO
     * @return
     */
    @PostMapping("/add")
    public ResultInfo addSource(@RequestBody @Validated SourcePO sourcePO){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        sourcePO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(sourcePO);

        sourceService.createSource(sourcePO);
        return ResultInfoUtil.success(TipMsgConstant.ADD_SOURCE_SUCCESS);
    }

    /**
     * 编辑来源
     * @param sourcePO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editSource(@RequestBody SourcePO sourcePO){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        sourcePO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(sourcePO);
        if (StringUtil.isNullStr(String.valueOf(sourcePO.getId())))
            throw new RException(ExceptionEnum.SOURCE_ID_NULL);
        sourceService.editSource(sourcePO);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SOURCE_SUCCESS);
    }

    /**
     * 删除来源
     * @param id
     * @return
     */
    @GetMapping("/del")
    public ResultInfo delSource(@NotEmpty int id){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();
        sourceService.delSourceById(id,companyId);

        return ResultInfoUtil.success(TipMsgConstant.DEL_SOURCE_SUCCESS);
    }
}
