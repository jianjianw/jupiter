package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;
import com.qiein.jupiter.web.entity.vo.StaffDetailVO;

/**
 * 员工详细信息
 */
public class StaffDetailPO extends BaseEntity {

    private static final long serialVersionUID = 1627067701096255485L;

    /**
     * 微信名称
     */
    private String weChatName;
    /**
     * 微信头像
     */
    private String weChatImg;
    /**
     * qq
     */
    private String qq;
    /**
     * 上次登录时间
     */
    private int lastLoginTime;
    /**
     * 上次登录IP
     */
    private String lastLoginIp;
    /**
     * Openid
     */
    private String weChatOpenId;
    /**
     * 创建时间
     */
    private int createTime;
    /**
     * 公司ID
     */
    private int companyId;

    public StaffDetailPO() {

    }

    /**
     * 根据员工信息生成的
     *
     * @param staffDetailVO
     */
    public StaffDetailPO(StaffDetailVO staffDetailVO) {
        this.setId(staffDetailVO.getId());
        this.companyId = staffDetailVO.getCompanyId();
        this.weChatName = staffDetailVO.getWeChatName();
        this.weChatImg = staffDetailVO.getWeChatImg();
        this.lastLoginTime = staffDetailVO.getLastLoginTime();
        this.lastLoginIp = staffDetailVO.getLastLoginIp();
        this.weChatOpenId = staffDetailVO.getWeChatOpenId();
        this.createTime = staffDetailVO.getCreateTime();
        this.qq = staffDetailVO.getQq();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeChatName() {
        return weChatName;
    }

    public void setWeChatName(String weChatName) {
        this.weChatName = weChatName;
    }

    public String getWeChatImg() {
        return weChatImg;
    }

    public void setWeChatImg(String weChatImg) {
        this.weChatImg = weChatImg;
    }

    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
