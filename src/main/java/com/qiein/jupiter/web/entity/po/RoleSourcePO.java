package com.qiein.jupiter.web.entity.po;

/**
 * FileName: RoleSourcePO
 *
 * @author: yyx
 * @Date: 2018-7-18 15:46
 */
public class RoleSourcePO {
    /**
     * id
     * */
    private Integer id;

    /**
     * 角色id
     * */
    private Integer roleId;

    /**
     * 来源id
     * */
    private Integer sourceId;

    /**
     * 公司id
     * */
    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
