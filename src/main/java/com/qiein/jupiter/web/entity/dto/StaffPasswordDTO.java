package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;

/**
 * 用户密码对象
 */
public class StaffPasswordDTO {
    private int id;

    @NotEmptyStr(message = "原密码不能为空")
    private String oldPassword;

    @NotEmptyStr(message = "新密码不能为空")
    private String newPassword;

    private int companyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
