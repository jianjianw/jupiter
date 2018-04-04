package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;


/**
 * 权限数据库对象
 */
public class PermissionPO extends BaseEntity{

    private static final long serialVersionUID = -8520233999874945394L;

    private int permissionId;
    private String pmsName;
    private String method;
    private String description;
    private int applicationId;
    private int dingAppId;
    private int priority;
    private String pmsType;

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPmsName() {
        return pmsName;
    }

    public void setPmsName(String pmsName) {
        this.pmsName = pmsName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getDingAppId() {
        return dingAppId;
    }

    public void setDingAppId(int dingAppId) {
        this.dingAppId = dingAppId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPmsType() {
        return pmsType;
    }

    public void setPmsType(String pmsType) {
        this.pmsType = pmsType;
    }
}
