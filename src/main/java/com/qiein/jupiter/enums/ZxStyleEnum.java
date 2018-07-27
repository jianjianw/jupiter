package com.qiein.jupiter.enums;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;

import java.util.List;

/**
 * FileName: ZxStyleEnum
 *
 * @author: yyx
 * @Date: 2018-7-25 16:08
 */
public enum ZxStyleEnum {
    TALK_ZX(1,"对话咨询"),
    PHONE_ZX(2,"电话咨询"),
    ENROLL_ZX(3,"报名咨询");

    private int zxType;
    private String zxStyle;

    ZxStyleEnum(int zxType,String zxStyle){
        this.zxType = zxType;
        this.zxStyle = zxStyle;
    }

    /**
     * 根据角色，获取对应的渠道类型集合
     *
     * @param zxStyle
     * @return
     */
    public static int getZxType(String zxStyle) {
        for (ZxStyleEnum zxStyleEnum : ZxStyleEnum.values()) {
            if (zxStyle.equals(zxStyleEnum.getZxStyle())) {
                return zxStyleEnum.getZxType();
            }
        }
        throw new RException(ExceptionEnum.ZX_STYLE_NOT_EXISTS);
    }

    public int getZxType() {
        return zxType;
    }

    public void setZxType(int zxType) {
        this.zxType = zxType;
    }

    public String getZxStyle() {
        return zxStyle;
    }

    public void setZxStyle(String zxStyle) {
        this.zxStyle = zxStyle;
    }
}
