package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;
import java.util.Map;

/**
 * 员工
 */
public interface StaffService {


    /**
     * 员工新增
     *
     * @param
     * @return
     */
    int insert(StaffPO staffPO);

    /**
     * 员工删除（物理删除）
     *
     * @param
     * @return
     */
    int delete(StaffPO staffPO);

    /**
     * 根据ID获取员工
     *
     * @return
     */
    StaffPO getById();

    /**
     * 根据条件查询
     *
     * @param map 查询条件
     * @return
     */
    List<StaffPO> findList(Map map);

    /**
     * 登录
     *
     * @param userName  用户名
     * @param password  密码
     * @param companyId 公司id
     * @return
     */
    StaffPO Login(String userName, String password, int companyId);

}
