package com.qiein.jupiter.enums;

/**
 * 员工状态枚举
 *
 * @Author: shiTao
 */
public enum StaffStatusEnum {
    /**
     * 离线
     */
    OffLine(0),
    /**
     * 上线
     */
    OnLine(1),
    /**
     * 停单
     */
    STOP_ORDER(8),
    /**
     * 满限
     */
    LIMIT(9);

    private int statusId;

    StaffStatusEnum(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }
}
