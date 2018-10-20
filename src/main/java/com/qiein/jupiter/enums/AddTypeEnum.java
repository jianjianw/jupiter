package com.qiein.jupiter.enums;

import com.qiein.jupiter.util.NumUtil;

/**
 * @Author: shiTao
 */
public enum AddTypeEnum {

    PC(1, "网页端"),

    MOBILE(2, "手机端"),

    GOLD(3, "金数据");

    private final int typeId;
    private final String typeName;

    AddTypeEnum(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public static String getTypeNameById(int typeId) {
        if (!NumUtil.isValid(typeId)) {
            return "其他";
        }
        for (AddTypeEnum typeEnum : AddTypeEnum.values()) {
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
