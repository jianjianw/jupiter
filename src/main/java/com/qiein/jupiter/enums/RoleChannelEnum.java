package com.qiein.jupiter.enums;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;

import java.util.List;

public enum RoleChannelEnum {

    DSCJ(RoleConstant.DSCJ, ChannelConstant.DS_TYPE_LIST),
    DSSX(RoleConstant.DSSX, ChannelConstant.DS_TYPE_LIST),
    DSYY(RoleConstant.DSYY, ChannelConstant.DS_TYPE_LIST),
    ZJSSX(RoleConstant.ZJSSX,ChannelConstant.ZJS_TYPE_LIST),
    ZJSYY(RoleConstant.ZJSYY, ChannelConstant.ZJS_TYPE_LIST),
    MSJD(RoleConstant.MSJD, ChannelConstant.ALL_TYPE_LIST),
    GLZX(RoleConstant.GLZX, ChannelConstant.ALL_TYPE_LIST),
    CWZX(RoleConstant.CWZX, ChannelConstant.ALL_TYPE_LIST);

    private String role;
    private List<Integer> typeList;

    /**
     * 根据角色，获取对应的渠道类型集合
     *
     * @param role
     * @return
     */
    public static List<Integer> getTypeListByRole(String role) {
        for (RoleChannelEnum roleChannel : RoleChannelEnum.values()) {
            if (role.equals(roleChannel.getRole())) {
                return roleChannel.getTypeList();
            }
        }
        throw new RException(ExceptionEnum.ROLE_ERROR);
    }

    RoleChannelEnum(String role, List<Integer> typeList) {
        this.role = role;
        this.typeList = typeList;
    }

    public String getRole() {
        return role;
    }

    public List<Integer> getTypeList() {
        return typeList;
    }
}
