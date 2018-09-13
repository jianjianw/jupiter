package com.qiein.jupiter.web.entity.vo;

import java.util.List;

public class RoleSourceVO {
    //权限id
    private Integer roleId;
    //来源ids
    private List<Integer> list;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
