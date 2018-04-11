package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;

/**
 * 组与员工表
 */
public class GroupStaffPO implements Serializable {

    private static final long serialVersionUID = -5321892693922336980L;
    /**
     * 组id
     */
    private String groupId;

    /**
     * 员工id
     */
    private int staffId;

    /**
     * 公司Id
     */
    private int companyId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
