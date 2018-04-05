package com.qiein.jupiter.web.service;

import com.github.pagehelper.Page;
import com.qiein.jupiter.web.dao.BaseDao;
import com.qiein.jupiter.web.dao.StaffDao;

import java.util.List;
import java.util.Map;

/**
 * 员工
 */
public interface StaffService<T> {


    /**
     * 员工新增
     *
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 员工删除（物理删除）
     *
     * @param t
     * @return
     */
    int delete(T t);

    /**
     * 根据ID获取员工
     *
     * @return
     */
    T getById();

    /**
     * 根据条件查询
     *
     * @param map 查询条件
     * @return
     */
    List<T> findList(Map map);

    /**
     * 登录
     *
     * @param userName  用户名
     * @param password  密码
     * @param companyId 公司id
     * @return
     */
    T Login(String userName, String password, int companyId);

}
