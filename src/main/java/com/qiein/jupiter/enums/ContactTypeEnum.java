package com.qiein.jupiter.enums;

import com.qiein.jupiter.util.NumUtil;

/**
 * @Author: shiTao
 */
public enum ContactTypeEnum {

    KZ_PHONE(1, "手机号"),
    KZ_WECHAT(2, "微信号"),
    KZ_QQ(3, "QQ号"),
    KZ_WW(4, "旺旺号"),
    MATE_PHONE(5, "闺蜜手机号"),
    MATE_WECHAT(6, "闺蜜微信号"),
    MATE_QQ(7, "闺蜜QQ号"),
    MATE_WW(8, "闺蜜旺旺号");

    private final int typeId;
    private final String typeName;

    private ContactTypeEnum(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public static String getTypeNameById(int typeId) {
        if (!NumUtil.isValid(typeId)) {
            return "其他";
        }
        for (ContactTypeEnum typeEnum : ContactTypeEnum.values()) {
            if (typeEnum.getTypeId() == typeId) {
                return typeEnum.getTypeName();
            }
        }
        return "其他";
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }
}
