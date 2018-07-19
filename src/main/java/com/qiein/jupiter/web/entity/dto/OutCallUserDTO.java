package com.qiein.jupiter.web.entity.dto;

/**
 * 天润外呼系统用户
 *
 * @Author: shiTao
 */
public class OutCallUserDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 企业Id
     */
    private String enterpriseId;
    /**
     * 加密字符
     */
    private String seed;
    /**
     * 坐席ID
     */
    private int id;
    /**
     * 坐席工号 2000-9999
     */
    private int cno;
    /**
     * 坐席姓名
     */
    private String name;
    /**
     * 绑定的手机号码
     */
    private String bindTel;

    /**
     * 区号
     */
    private String areaCode;
    /**
     * 状态
     */
    private int status;
    /**
     * 电话类型
     */
    private int type;
    /**
     * 解除绑定电话 0：不解除 1：解除   默认不解除
     */
    private int unBind;

    /**
     * crm企业Id
     */
    private int companyId;
    /**
     * crm 员工ID
     */
    private int staffId;


    public int getCno() {
        return cno;
    }

    public void setCno(int cno) {
        this.cno = cno;
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

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBindTel() {
        return bindTel;
    }

    public void setBindTel(String bindTel) {
        this.bindTel = bindTel;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnBind() {
        return unBind;
    }

    public void setUnBind(int unBind) {
        this.unBind = unBind;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
