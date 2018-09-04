package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.entity.vo.SourceVO;
import com.qiein.jupiter.web.service.SourceService;
import com.qiein.jupiter.web.service.SystemLogService;
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
    @Autowired
    private SystemLogService logService;

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
        //添加日志
        try {
            RequestInfoDTO requestInfo = getRequestInfo();
            // 日志记录
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_SOURCE, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_SOURCE, sourcePO.getSrcName()), currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfoUtil.success(TipMsgEnum.ADD_SOURCE_SUCCESS);
        }
        return ResultInfoUtil.success(TipMsgEnum.ADD_SOURCE_SUCCESS);
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
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SOURCE_SUCCESS);
    }

    /**
     * 拖拽排序
     *
     * @param fPriority 拖拽前的来源排序编号
     * @param sPriority 拖拽后的来源排序编号
     * @param id        拖拽的来源编号
     * @return
     */
    @GetMapping("/tz_priority")
    public ResultInfo editPriority(@Id Integer fPriority, @Id Integer sPriority, @Id Integer id) {
        sourceService.editSourcePriority(fPriority, sPriority, id, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SOURCE_SUCCESS);
    }

    /**
     * 编辑渠道排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @return
     */
    @GetMapping("/priority")
    public ResultInfo editPriority(Integer fId, Integer fPriority, Integer sId, Integer sPriority) {
        sourceService.editProiority(fId, fPriority, sId, sPriority, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
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
        return ResultInfoUtil.success(TipMsgEnum.DEL_SOURCE_SUCCESS);
    }

    /**
     * 获取该员工的所有来源
     *
     * @param staffId
     * @return
     */
    @GetMapping("/get_staff_source_list")
    public ResultInfo getStaffSourceList(Integer staffId) {
        if (staffId == null) {
            throw new RException(ExceptionEnum.STAFF_ID_NULL);
        }
        return ResultInfoUtil.success(sourceService.getStaffSourceList(getCurrentLoginStaff().getCompanyId(), staffId));
    }
}
