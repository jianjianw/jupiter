package com.qiein.jupiter.web.entity.vo;

/**
 * 精简版 员工对象
 *
 * @Author: shiTao
 */
public class SimpleStaffVO {

    private int id;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户名
     */
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
