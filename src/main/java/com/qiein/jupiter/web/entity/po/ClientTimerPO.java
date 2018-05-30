package com.qiein.jupiter.web.entity.po;

/**
 * 客资追踪提醒
 *
 * @Author: shiTao
 */
public class ClientTimerPO {
    private int id;
    /**
     * 报警时间
     */
    private int warnTime;
    /**
     * 员工Id
     */
    private int staffId;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 客资Id
     */
    private String kzId;
    /**
     * 公司ID
     */
    private int companyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(int warnTime) {
        this.warnTime = warnTime;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
