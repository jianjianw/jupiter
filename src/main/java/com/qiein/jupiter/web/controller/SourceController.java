package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.SourceVO;
import com.qiein.jupiter.web.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 来源Controller
 */
@RestController
@RequestMapping("/source")
@Validated
public class SourceController extends BaseController {

    @Autowired
    private SourceService sourceService;

    /**
     * 新增来源
     *
     * @param sourcePO
     * @return
     */
    @PostMapping("/add")
    public ResultInfo addSource(@RequestBody @Validated SourcePO sourcePO) {
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
     *
     * @param sourceVO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editSource(@RequestBody SourceVO sourceVO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        sourceVO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(sourceVO);
        if (StringUtil.isEmpty(String.valueOf(sourceVO.getId())))
            throw new RException(ExceptionEnum.SOURCE_ID_NULL);
        sourceService.editSource(sourceVO);
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SOURCE_SUCCESS);
    }

    /**
     * 拖拽排序
     * @param fPriority 前一个排序号
     * @param sPriority 后一个排序号
     * @param id        拖拽的来源编号
     * @return
     */
    @GetMapping("/priority")
    public ResultInfo editPriority(@Id Integer fPriority,@Id Integer sPriority,@Id Integer id) {
        sourceService.editSourcePriority(fPriority, sPriority, id, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SOURCE_SUCCESS);
    }

    /**
     * 删除来源
     *
     * @param ids
     * @return
     */
    @GetMapping("/del")
    public ResultInfo delSource(@NotEmptyStr String ids) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //获取所属公司编号
        Integer companyId = currentLoginStaff.getCompanyId();
        sourceService.datDelSrc(ids, companyId);

        return ResultInfoUtil.success(TipMsgConstant.DEL_SOURCE_SUCCESS);
    }
}
