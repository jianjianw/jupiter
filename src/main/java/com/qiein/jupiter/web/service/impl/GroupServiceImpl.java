package com.qiein.jupiter.web.service.impl;

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
    private GroupDao groupDao;//部门持久层

    /**
     *
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


    @Override
    public int update(GroupPO groupPO) {
        return 0;
    }

    @Override
    public int delete(int companyId) {
        return 0;
    }

    @Override
    public int insert(GroupPO groupPO) {
        return 0;
    }
}
