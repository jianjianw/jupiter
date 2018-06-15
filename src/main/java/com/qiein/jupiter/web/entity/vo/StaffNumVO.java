package com.qiein.jupiter.web.entity.vo;

/**
 * 员工，客资数量，客资ID
 * @author gaoxiaoli 2018/6/15
 */

public class StaffNumVO {

    private static final long serialVersionUID = 1L;

    private Integer staffId;
    private Integer num;
    private String kzId;

    public boolean isEmpty() {

        return (staffId == null || staffId == 0 || num == null || num == 0);
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }
}
