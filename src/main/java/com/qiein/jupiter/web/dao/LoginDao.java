package com.qiein.jupiter.web.dao;

/**
 * 登录Mapper
 */
public interface LoginDao {

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    int Login(String userName, String password);
}
