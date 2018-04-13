package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.util.ListUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import com.qiein.jupiter.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /*获取公司所有部门和小组*/
    public List<GroupVO> getCompanyAllDeptList(Integer companyId) {
        List<GroupVO> grouplist = groupDao.getCompanyAllDeptList(companyId);
        return removeNullDataFromList(grouplist);
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
}
