package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.GroupStaffPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.GroupService;
import com.qiein.jupiter.web.service.GroupStaffService;
import com.qiein.jupiter.web.service.StaffService;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FileName: GroupStaffController
 *
 * @author: yyx
 * @Date: 2018-6-27 10:05
 */
@RestController
@RequestMapping("/group_staff")
public class GroupStaffController extends BaseController{
    @Autowired
    private GroupStaffService groupStaffService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private SystemLogService logService;
    /**
     *  插入部门员工关系
     */
    @PostMapping("/insert")
    public ResultInfo insert(@RequestBody JSONObject jsonObject){
        StaffPO staffPO = getCurrentLoginStaff();
        RequestInfoDTO requestInfo = getRequestInfo();
        groupStaffService.insert(jsonObject,staffPO);
        try{
            String staffIds=jsonObject.getString("staffIds");
            String groupId=jsonObject.getString("groupId");
            String groupName=groupService.getGroupName(Integer.parseInt(groupId));
            List<String>  staffNames=staffService.getStaffNames(staffIds);
            StringBuffer staffNamess=new StringBuffer();
            for(String staffName:staffNames){
                staffNamess.append(staffName+CommonConstant.STR_SEPARATOR);
            }
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_GROUP, requestInfo.getIp(), requestInfo.getUrl(), staffPO.getId(),
                    staffPO.getNickName(), SysLogUtil.getAddLog(SysLogUtil.LOG_SUP_GROUP_STAFF, groupName,CommonConstant.ADD_TO,staffNamess.toString()),
                    staffPO.getCompanyId());
            logService.addLog(log);
        }catch (Exception e){
            e.printStackTrace();
            return ResultInfoUtil.success();
        }
        return ResultInfoUtil.success();
    }

    /**
     * 移除
     * */
    @PostMapping("/remove")
    public ResultInfo remove(@RequestBody GroupStaffPO groupStaffPO){
        StaffPO staffPO = getCurrentLoginStaff();
        groupStaffPO.setCompanyId(staffPO.getCompanyId());
        RequestInfoDTO requestInfo = getRequestInfo();
        groupStaffService.remove(groupStaffPO);
        try{
            String staffIds=groupStaffPO.getStaffId()+CommonConstant.NULL_STR;
            String groupId=groupStaffPO.getGroupId();
            String groupName=groupService.getGroupName(Integer.parseInt(groupId));
            List<String>  staffNames=staffService.getStaffNames(staffIds);
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_GROUP, requestInfo.getIp(), requestInfo.getUrl(), staffPO.getId(),
                    staffPO.getNickName(), SysLogUtil.getRemoveLog(SysLogUtil.LOG_SUP_GROUP_STAFF, groupName,CommonConstant.ADD_TO,staffNames.get(0)),
                    staffPO.getCompanyId());
            logService.addLog(log);
        }catch (Exception e){
            e.printStackTrace();
            return ResultInfoUtil.success();
        }
        return ResultInfoUtil.success();
    }
}
