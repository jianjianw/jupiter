package com.qiein.jupiter.enums;

/**
 * 来源类型枚举
 *
 * @Author: shiTao
 */
public enum SourceTypeEnum {
    //    类型ID 1:纯电商，2:电商转介绍，3:员工转介绍，4:指名转介绍，5:外部转介绍，6:自然入客，7:门店外展
    CDS(1, "纯电商"),
    DSZJS(2, "电商转介绍"),
    YGZJS(3, "员工转介绍"),
    ZMZJS(4, "指名转介绍"),
    WBZJS(5, "外部转介绍"),
    ZRRK(6, "自然入客"),
    MDWZ(7, "门店外展");

    private int typeId;

    private String typeName;

    SourceTypeEnum(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }


    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }
}
