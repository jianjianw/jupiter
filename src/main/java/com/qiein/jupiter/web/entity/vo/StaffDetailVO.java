package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * 员工详细信息
 */
public class StaffDetailVO extends StaffPO {

    private static final long serialVersionUID = 7687459749239616046L;
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
     * 所属小组编号
     */
    private String groupId;
    /**
     * 所属小组名称
     */
    private String groupName;
    /**
     * 所属部门编号
     */
    private String deptId;
    /**
     * 所属部门名称
     */
    private String deptName;
    /**
     * 上次登录时间
     */
    private int lastLoginTime;
    /**
     * 上传登录Ip
     */
    private String lastLoginIp;
    /**
     * wechat openid
     */
    private String weChatOpenId;
    /**
     * wechat union id
     */
    private String weChatUnionId;
    /**
     * ding openid
     */
    private String dingOpenId;
    /**
     * ding union id
     */
    private String dingUnionId;
    /**
     * 创建时间
     */
    private int createTime;

    /**
     * 是否是简单密码
     */
    private boolean simplePasswordFlag;

    /**
     * 个人消息设置
     */
    private String msgSet;

    public String getMsgSet() {
        return msgSet;
    }

    public void setMsgSet(String msgSet) {
        this.msgSet = msgSet;
    }

    public boolean isSimplePasswordFlag() {
        return simplePasswordFlag;
    }

    public void setSimplePasswordFlag(boolean simplePasswordFlag) {
        this.simplePasswordFlag = simplePasswordFlag;
    }

    public String getWeChatUnionId() {
        return weChatUnionId;
    }

    public void setWeChatUnionId(String weChatUnionId) {
        this.weChatUnionId = weChatUnionId;
    }

    public String getDingOpenId() {
        return dingOpenId;
    }

    public void setDingOpenId(String dingOpenId) {
        this.dingOpenId = dingOpenId;
    }

    public String getDingUnionId() {
        return dingUnionId;
    }

    public void setDingUnionId(String dingUnionId) {
        this.dingUnionId = dingUnionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
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
}
