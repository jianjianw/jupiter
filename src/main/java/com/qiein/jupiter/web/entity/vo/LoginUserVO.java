package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.constant.CommonConstants;
import com.qiein.jupiter.constant.VerifyConstants;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * 登录用户业务对象
 */
public class LoginUserVO {

    /**
     * 用户名
     */
    @NotEmpty(message = VerifyConstants.USER_NAME_NOT_NULL)
    private String userName;

    /**
     * 密码
     */
    @NotEmpty(message = VerifyConstants.PASS_WORD_NOT_NULL)
    private String password;

    private int companyId;

    /**
     * 验证码
     */
    private String verifyCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
