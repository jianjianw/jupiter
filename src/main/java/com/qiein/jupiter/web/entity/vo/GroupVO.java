package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.GroupPO;

import java.util.List;

/**
 *
 */
public class GroupVO extends GroupPO {

    private List<GroupPO> groupList;//小组集合


    public List<GroupPO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupPO> groupList) {
        this.groupList = groupList;
    }
}
