package com.qiein.jupiter.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;
import com.qiein.jupiter.web.service.GroupService;

/**
 * 网销排班
 * Created by Administrator on 2018/5/8 0008.
 */
@RestController
@RequestMapping("/staffmars")
public class StaffMarsController extends BaseController{

    @Resource
    private GroupService groupService;

    /**
     * 根据类型排班信息
     * @param type  ds 电商 zjs 转介绍
     * @return
     */
    @GetMapping("/get_base_info")
    public ResultInfo getStaffMarsInfo(String type){
        List<GroupsInfoVO> list = groupService.getCompanyDeptListByType(type,getCurrentLoginStaff().getId(),getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS,list);
    }
}
