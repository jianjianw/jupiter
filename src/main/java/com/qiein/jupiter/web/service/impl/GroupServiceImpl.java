package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.PmsConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ListUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.dao.RolePermissionDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * 部门小组实现类
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * @param companyId
     * @return
     */
    //@Cacheable(value = "dept", key = "'dept'+':'+#companyId")
    public List<GroupVO> getCompanyAllDeptList(int companyId) {
        return groupDao.getCompanyAllDeptList(companyId);
    }

    /**
     * 修改部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    @Transactional
    public GroupPO update(GroupPO groupPO) {
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        //验证是否存在相同的部门名称
        if (groupDB != null && groupDB.getId() != groupPO.getId()) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        //判断是否需要设置主管姓名
        checkSetChiefsName(groupPO);
        groupDao.update(groupPO);
        //把下属的组的类别也更新
        List<GroupPO> byParentId = groupDao.getByParentId(groupPO.getGroupId(), groupPO.getCompanyId());
        if (ListUtil.isNotNullList(byParentId)) {
            for (GroupPO po : byParentId) {
                po.setGroupType(groupPO.getGroupType());
            }
            groupDao.batchUpdateGroupType(byParentId);
        }
        return groupPO;
    }

    /**
     * 删除部门信息
     *
     * @param companyId
     * @return
     */
    @Override
    public int delete(int id, int companyId) {
        //先判断是否有下属部门
        GroupPO groupPO = groupDao.getById(id);
        if (groupPO == null) {
            throw new RException(ExceptionEnum.GROUP_NOT_EXIT);
        }
        List<GroupPO> byParentId = groupDao.getByParentId(groupPO.getGroupId(), companyId);
        if (ListUtil.isNotNullList(byParentId)) {
            throw new RException(ExceptionEnum.GROUP_HAVE_CHILD_GROUP);
        }
        //是否有下属员工
        List<StaffVO> groupStaffs = groupStaffDao.getGroupStaffs(companyId, groupPO.getGroupId());
        if (ListUtil.isNotNullList(groupStaffs)) {
            throw new RException(ExceptionEnum.GROUP_HAVE_STAFF);
        }
        return groupDao.delete(id);
    }

    /**
     * 新增部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    @Transactional
    public GroupPO insert(GroupPO groupPO) {
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        //验证是否存在相同的部门名称
        if (groupDB != null) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        //判断是否需要设置主管姓名
        checkSetChiefsName(groupPO);
        //新增
        groupDao.insert(groupPO);
        //把组id 设置为父类Id+  分隔符  + 新增的id
        groupPO.setGroupId(groupPO.getParentId() + CommonConstant.ROD_SEPARATOR + groupPO.getId());
        groupDao.update(groupPO);
        return groupPO;
    }

    /**
     * 获取所有部门和员工
     *
     * @param companyId
     * @return
     */
    @Override
    public List<GroupStaffVO> getAllDeptAndStaff(int companyId) {
        return groupStaffDao.getAllDeptAndStaff(companyId);
    }

    /**
     * 设置主管姓名
     */
    private void checkSetChiefsName(GroupPO groupPO) {
        String chiefIds = groupPO.getChiefIds();
        //如果id不为空
        if (StringUtil.isNotEmpty(chiefIds)) {
            //根据ids获取员工数组
            String[] split = chiefIds.split(CommonConstant.STR_SEPARATOR);
            List<StaffPO> staffPOS = staffDao.batchGetByIds(split, groupPO.getCompanyId());
            StringBuilder chiefNames = new StringBuilder();
            //遍历追加姓名
            for (int i = 0; i < staffPOS.size(); i++) {
                chiefNames.append(staffPOS.get(i).getNickName());
                if (i < (staffPOS.size() - 1)) {
                    chiefNames.append(CommonConstant.STR_SEPARATOR + " ");
                }
            }
            //设置
            groupPO.setChiefNames(chiefNames.toString());
        }
    }

    /**
     * 根据不同角色，获取对应小组人员
     *
     * @param companyId
     * @param staffId
     * @return
     */
    public List<GroupBaseStaffVO> getGroupStaffByType(int companyId, int staffId, String role) {
        //1、查询所有小组人员
        List<GroupBaseStaffVO> groupList = groupStaffDao.getGroupStaffByRole(companyId, role);
        //2.查询员工权限
        List<Integer> pmsList = rolePermissionDao.getStaffPmsList(companyId, staffId);

        List<String> groupIdList = null;
        Integer id = null;
        boolean all = false;
        if (pmsList.contains(PmsConstant.SEE_MYSELF)) {
            // 只看自己
            id = staffId;
        } else if (pmsList.contains(PmsConstant.SEE_MY_GROUP)) {
            // 只看本组
            groupIdList = groupDao.getGroupByStaffAndType(companyId, staffId, role);
        } else if (pmsList.contains(PmsConstant.SEE_MY_DEPT)) {
            // 查看部门
            groupIdList = groupDao.getDeptByStaffAndType(companyId, staffId, role);
        } else if (pmsList.contains(PmsConstant.SEE_ALL)) {
            //查看所有
            all = true;
        }
        for (GroupBaseStaffVO grp : groupList) {
            //只看自己
            if (id != null && ListUtil.isNotNullList(grp.getStaffList())) {
                for (BaseStaffVO staff : grp.getStaffList()) {
                    if (staff.getStaffId() == id.intValue()) {
                        staff.setSelectFlag(true);
                    }
                }
                continue;
            }
            //只看本组或只看部门
            if (ListUtil.isNotNullList(groupIdList) && groupIdList.contains(grp.getGroupId()) && ListUtil.isNotNullList(grp.getStaffList())) {
                grp.setSelectFlag(true);
                for (BaseStaffVO staff : grp.getStaffList()) {
                    staff.setSelectFlag(true);
                }
                continue;
            }
            //查看所有
            if (all && ListUtil.isNotNullList(grp.getStaffList())) {
                grp.setSelectFlag(true);
                for (BaseStaffVO staff : grp.getStaffList()) {
                    staff.setSelectFlag(true);
                }
                continue;
            }

        }
        return groupList;
    }
}
