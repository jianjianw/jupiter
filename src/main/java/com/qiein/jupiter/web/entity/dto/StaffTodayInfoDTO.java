package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * 微信个人中心页面需要的员工信息
 * @create by Tt(叶华葳) 2018-06-12 11:34
 */
public class StaffTodayInfoDTO extends BaseEntity{

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 状态
     */
    private Integer statusFlag;
    /**
     * 今日接单数
     */
    private Integer todayNum;
    /**
     * 今日怠工数
     */
    private Integer allot;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Integer getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(Integer todayNum) {
        this.todayNum = todayNum;
    }

    public Integer getAllot() {
        return allot;
    }

    public void setAllot(Integer allot) {
        this.allot = allot;
    }

}
