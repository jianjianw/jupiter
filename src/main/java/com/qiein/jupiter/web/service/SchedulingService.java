package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;

/**
 * 网销排班Service
 * Created by Tt(叶华葳)
 * on 2018/5/10 0010.
 */
public interface SchedulingService {

    /**
     * 获取公司所有符合类型的部门列表
     * @param type
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getDeptListByType(String type, int companyId, int staffId);

    /**
     * 过滤员工权限和部门今日接单和当前在线人数
     * @param list
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> filterDeptList(List<GroupsInfoVO> list,int companyId,int staffId);

    /**
     * 获取部门下属的小组列表
     * @param deptId
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getGroupListByDept(String deptId,int companyId, int staffId);

    /**
     * 过滤员工权限和小组今日接单和当前在线人数
     * @param list
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> filterGroupList(List<GroupsInfoVO> list,String deptId,int companyId);

    /**
     * 编辑员工状态
     *
     * @param staffMarsDTO
     */
    void editStaffMars(StaffMarsDTO staffMarsDTO);
}
