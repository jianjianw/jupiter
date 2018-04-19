package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ListUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import com.qiein.jupiter.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * 部门小组
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupStaffDao groupStaffDao;

    /**
     * @param companyId
     * @return
     */
    //@Cacheable(value = "dept", key = "'dept'+':'+#companyId")
    public List<GroupVO> getCompanyAllDeptList(int companyId) {
        List<GroupVO> groupList = groupDao.getCompanyAllDeptList(companyId);
        return groupList;
    }

    /**
     * 修改部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    public GroupPO update(GroupPO groupPO) {
        List<GroupPO> groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        //验证是否存在相同的部门名称
        if (ListUtil.isNotNullList(groupDB)) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        groupDao.update(groupPO);
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
        List<GroupPO> byParentId = groupDao.getByParentId(groupPO.getGroupId(), companyId);
        if (ListUtil.isNotNullList(byParentId)) {
            throw new RException(ExceptionEnum.GROUP_HAVE_CHILD_GROUP);
        }
        //是否有下属员工
        List<StaffVO> groupStaffs = groupStaffDao.getGroupStaffs(companyId, groupPO.getParentId());
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
    public GroupPO insert(GroupPO groupPO) {
        List<GroupPO> groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        //验证是否存在相同的部门名称
        if (ListUtil.isNotNullList(groupDB)) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        //新增
        groupDao.insert(groupPO);
        //把组id 设置为父类Id+  分隔符  + 新增的id
        groupPO.setGroupId(groupPO.getParentId() + CommonConstant.ROD_SEPARATOR + groupPO.getId());
        groupDao.update(groupPO);
        return groupPO;
    }
}
