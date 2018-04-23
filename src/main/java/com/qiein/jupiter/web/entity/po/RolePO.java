package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 角色数据库对象
 */
public class RolePO extends BaseEntity {

    private static final long serialVersionUID = 6090149708646334672L;

    /**
     * 角色名称
     */
    @NotEmptyStr(message = "{role.roleName.null}")
    private String roleName;
    /**
     * 公司id
     */
    private int companyId;
    /**
     * 排序
     */
    private int priority;

    public RolePO() {
    }

    public RolePO(String roleName, int companyId) {
        this.roleName = roleName;
        this.companyId = companyId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
