package com.qiein.jupiter.enums;

import com.qiein.jupiter.util.NumUtil;

/**
 * @Author: shiTao
 */
public enum QueryTimeTypeEnum {

    CREATE(1, "CREATETIME"), // 录入时间
    RECEIVE(2, "RECEIVETIME"), // 领取时间
    APPOINT(3, "APPOINTTIME"), // 预约时间
    COMESHOP(4, "COMESHOPTIME"), // 进店时间
    SUCCESS(5, "SUCCESSTIME"), // 订单时间
    TRACE(6, "TRACETIME"), // 下次追踪时间
    UPDATE(7, "UPDATETIME");// 最后操作时间

    /**
     * 默认时间类型
     */
    private static final String DEFAULT_TIME_TYPE = "CREATETIME";

    private final int typeId;
    private final String timeType;

    private QueryTimeTypeEnum(int typeId, String timeType) {
        this.typeId = typeId;
        this.timeType = timeType;
    }

    public static String getTimeTypeById(int typeId) {
        if (!NumUtil.isValid(typeId)) {
            return DEFAULT_TIME_TYPE;
        }
        for (QueryTimeTypeEnum typeEnum : QueryTimeTypeEnum.values()) {
            if (typeEnum.getTypeId() == typeId) {
                return typeEnum.getTimeType();
            }
        }
        return DEFAULT_TIME_TYPE;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTimeType() {
        return timeType;
    }
}