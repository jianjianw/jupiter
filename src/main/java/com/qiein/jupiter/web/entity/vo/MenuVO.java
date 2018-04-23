package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

public class MenuVO implements Serializable {
    /**
     * 类型
     */
    private String type;

    /**
     * 名称
     */
    private String dicName;

    /**
     * 是否可选
     */
    private boolean selectFlag;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }
}
