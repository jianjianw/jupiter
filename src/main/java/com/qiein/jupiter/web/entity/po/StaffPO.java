package com.qiein.jupiter.web.entity.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.aop.validate.annotation.Phone;
import com.qiein.jupiter.web.entity.BaseEntity;
import com.qiein.jupiter.web.entity.vo.StaffDetailVO;

/**
 * 员工数据库对象
 */
public class StaffPO extends BaseEntity {

    private static final long serialVersionUID = 3690836371907136145L;

    /**
     * 昵称
     */
    @NotEmptyStr(message = "{staff.name.null}")
    private String nickName;
    /**
     * 手机号码
     */
    @NotEmptyStr(message = "{staff.phone.null}")
    @Phone
    private String phone;
    /**
     * 全名
     */
    @NotEmptyStr(message = "{staff.userName.null}")
    private String userName;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;
    /**
     * 企业ID
     */
    private int companyId;
    /**
     * 钉钉企业ID
     */
    private String corpId;
    /**
     * 钉钉用户ID
     */
    private String dingUserId;
    /**
     * 微信
     */
    private String openId;
    /**
     * 头像
     */
    private String headImg;
    /**
     * token
     */
    private String token;
    /**
     * 在线状态 0 下线 1 在线
     */
    private Boolean showFlag;
    /**
     * 是否锁定
     */
    private boolean lockFlag;
    /**
     * 是否删除
     */
    private boolean delFlag;

    public StaffPO() {

    }

    /**
     * 根据员工详细信息初始化一个类
     *
     * @param staffDetailVO
     */
    public StaffPO(StaffDetailVO staffDetailVO) {
        this.setId(staffDetailVO.getId());
        this.nickName = staffDetailVO.getNickName();
        this.phone = staffDetailVO.getPhone();
        this.userName = staffDetailVO.getUserName();
        this.password = staffDetailVO.getPassword();
        this.companyId = staffDetailVO.getCompanyId();
        this.corpId = staffDetailVO.getCorpId();
        this.dingUserId = staffDetailVO.getDingUserId();
        this.headImg = staffDetailVO.getHeadImg();
        this.token = staffDetailVO.getToken();
        this.showFlag = staffDetailVO.getShowFlag();
        this.lockFlag = staffDetailVO.isLockFlag();
        this.delFlag = staffDetailVO.isDelFlag();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getDingUserId() {
        return dingUserId;
    }

    public void setDingUserId(String dingUserId) {
        this.dingUserId = dingUserId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }

    public boolean isLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(boolean lockFlag) {
        this.lockFlag = lockFlag;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }
}