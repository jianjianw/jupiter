package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.po.GroupStaffPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * FileName: GroupStaffService
 *
 * @author: yyx
 * @Date: 2018-6-27 10:10
 */
public interface GroupStaffService {

    /**
     * 插入组与用户的关系
     * @param jsonObject
     * @param staffPO
     * */
    void insert(JSONObject jsonObject, StaffPO staffPO);

    /**
     * 移除组与用户的关系
     * @param groupStaffPO
     * */
    void remove(GroupStaffPO groupStaffPO);

}
