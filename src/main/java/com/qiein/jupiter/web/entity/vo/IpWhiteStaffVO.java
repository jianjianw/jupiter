package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * ip白名单员工信息页面
 *
 * @Author: XiangLiang
 */
public class IpWhiteStaffVO implements Serializable {
    private static final long serialVersionUID = -5261162070419842309L;
    /**
     * 员工id
     */
    private int id;
    /**
     * 头像地址
     */
    private String headImg;
    /**
     * 员工名称
     */
    private String nickName;
    /**
     * 员工电话
     */
    private String phone;
    /**
     * 部门名称
     */
    private String groupName;
    /**
     * 账号创建时间
     */
    private int createTime;
    /**
     * 最后登陆时间
     */
    private int lastLoginTime;
    /**
     * 最后登录ip
     */
    private String lastLoginIp;
    /**
     * 最后登陆地址
     */
    private String ipLocation;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
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

    public String getIpLocation() {
        return ipLocation;
    }

    public void setIpLocation(String ipLocation) {
        this.ipLocation = ipLocation;
    }
}
