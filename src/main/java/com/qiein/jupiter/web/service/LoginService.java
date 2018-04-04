package com.qiein.jupiter.web.service;

/**
 * 登录接口
 */
public interface LoginService {

    /**
     * 登录
     *
     * @return
     */
    String login(String userName, String password);

    /**
     * 登出
     *
     * @return
     */
    String logout();
}
