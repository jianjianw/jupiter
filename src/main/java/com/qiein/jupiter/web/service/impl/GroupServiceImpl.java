package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ListUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
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

    /**
     * @param companyId
     * @return
     */
    @Cacheable(value = "dept", key = "'dept'+':'+#companyId")
    public List<GroupVO> getCompanyAllDeptList(int companyId) {
        List<GroupVO> groupList = groupDao.getCompanyAllDeptList(companyId);
        return removeNullDataFromList(groupList);
    }

    /*去掉集合里的空对象*/
    public List<GroupVO> removeNullDataFromList(List<GroupVO> list) {
        if (ListUtil.isNullList(list)) {
            return null;
        }
        for (GroupVO dept : list) {
            List<GroupPO> groupList = dept.getGroupList();
            if (ListUtil.isNotNullList(groupList)) {
                Iterator<GroupPO> it = groupList.iterator();
                while (it.hasNext()) {
                    GroupPO po = it.next();
                    if (StringUtil.isNullStr(po.getGroupId())) {
                        it.remove();
                    }
                }
            }

        }
        return list;
    }

    /**
     * 修改部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    public GroupPO update(GroupPO groupPO) {
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        //验证是否存在相同的部门名称
        if (StringUtil.ignoreCaseEqual(groupPO.getGroupName(), groupDB.getGroupName())) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        groupDao.update(groupDB);
        return groupPO;
    }

    /**
     * 删除部门信息
     *
     * @param companyId
     * @return
     */
    @Override
    public int delete(String groupId, int companyId) {
        //先判断是否有下属部门
        List<GroupPO> byParentId = groupDao.getByParentId(groupId, companyId);
        if (ListUtil.isNullList(byParentId)){
            throw new RException(ExceptionEnum.GROUP_HAVE_CHILD_GROUP);
        }
        //是否有下属员工

        return 0;
    }

    /**
     * 新增部门信息
     *
     * @param groupPO
     * @return
     */
    @Override
    public GroupPO insert(GroupPO groupPO) {
        GroupPO groupDB = groupDao.getByName(groupPO.getGroupName(), groupPO.getCompanyId());
        //验证是否存在相同的部门名称
        if (StringUtil.ignoreCaseEqual(groupPO.getGroupName(), groupDB.getGroupName())) {
            throw new RException(ExceptionEnum.GROUP_NAME_REPEAT);
        }
        groupDao.insert(groupPO);
        return groupPO;
    }
}
