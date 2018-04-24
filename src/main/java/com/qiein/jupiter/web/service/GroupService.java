package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupBaseStaffVO;
import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import com.qiein.jupiter.web.entity.vo.GroupVO;

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
    int delete(int id, int companyId);

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
     * @param type
     * @return
     */
    public List<GroupBaseStaffVO> getGroupStaffByType(int companyId, int staffId, String role);

}
