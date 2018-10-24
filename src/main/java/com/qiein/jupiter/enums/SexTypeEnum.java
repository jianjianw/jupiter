package com.qiein.jupiter.enums;

import com.qiein.jupiter.util.NumUtil;

/**
 * @Author: shiTao
 */
public enum SexTypeEnum {
    SEX_UNKNOWN(0, "未知"), SEX_MAIL(1, "男"), SEX_FEMALE(2, "女");

    private final int typeId;
    private final String typeName;

    SexTypeEnum(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public static String getNameById(int typeId) {
        if (!NumUtil.isValid(typeId)) {
            return "未知";
        }
        for (SexTypeEnum typeEnum : SexTypeEnum.values()) {
            if (typeEnum.getTypeId() == typeId) {
                return typeEnum.getTypeName();
            }
        }
        return "未知";
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }
}
