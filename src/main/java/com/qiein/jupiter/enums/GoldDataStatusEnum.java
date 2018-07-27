package com.qiein.jupiter.enums;

import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.sun.org.apache.regexp.internal.RE;

/**
 * FileName: GoldDataStatusEnum
 *
 * @author: yyx
 * @Date: 2018-7-26 11:26
 */
public enum  GoldDataStatusEnum {
    IN_FILTER(1,"筛选中"),
    HAVA_ENTERED(2,"已录入"),
    REPEATED_SCREEN(3,"重复屏蔽"),
    INTERCEPTING(4,"异地拦截"),
    IN_SUCCESS(5,"录入成功"),
    IN_FAIL(6,"录入失败");

    private Integer status;
    private String statusDesc;

    /**
     * 根据角色，获取对应的渠道类型集合
     *
     * @param status
     * @return
     */
    public static String getGoldStatusDesc(Integer status) {
        for (GoldDataStatusEnum goldDataStatusEnum:GoldDataStatusEnum.values()){
            if(goldDataStatusEnum.getStatus().equals(status)){
                return goldDataStatusEnum.getStatusDesc();
            }
        }
        throw  new RException(ExceptionEnum.UNKNOW_ERROR);
    }


    GoldDataStatusEnum(int status,String statusDesc){
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
