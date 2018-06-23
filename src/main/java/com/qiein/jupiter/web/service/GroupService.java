package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.*;

import java.util.List;

/**
 * 部门
 */
public interface GroupService {

    /**
     * 获取公司所有部门和小组
     *
     * @param companyId 公司id
     * @return
     */
    List<GroupVO> getCompanyAllDeptList(int companyId);

    /**
     * 根据当前权限和类型获取公司的部门
     *
     * @param type
     * @return
     */
    List<GroupsInfoVO> getStaffMarsInfo(String type, int staffId, int companyId);

    /**
     * 部门更新
     *
     * @param groupPO
     * @return
     */
    GroupPO update(GroupPO groupPO);

    /**
     * 部门删除
     *
     * @param companyId
     * @param id
     * @return
     */
    GroupPO delete(int id, int companyId);

    /**
     * 部门新增
     *
     * @param groupPO
     */
    GroupPO insert(GroupPO groupPO);

    /**
     * 获取公司所有的部门及下属员工
     *
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getAllDeptAndStaff(int companyId);

    /**
     * 根据不同角色，获取对应小组人员
     *
     * @param companyId
     * @param staffId
     * @return
     */
    List<GroupBaseStaffVO> getGroupStaffByType(int companyId, int staffId, String role);

    /**
     * 获取邀约客服小组及人员
     *
     * @param companyId
     * @return
     */
    List<GroupBaseStaffVO> getDsyyGroupStaffList(int companyId);

    /**
     * 获取门市下面的所有人员列表
     *
     * @param companyId
     * @return
     */
    List<BaseStaffVO> getMsjdStaffList(int companyId);

    /**
     * 获取全公司所有小组人员列表
     *
     * @param companyId
     * @return
     */
    List<GroupBaseStaffVO> getAllGroupStaff(int companyId);
}
