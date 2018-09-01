package com.qiein.jupiter.constant;

/**
 * @author: yyx
 * @Date: 2018-8-9
 */
public class AppolloUrlConst {
    /***
     * 所有呼叫中心实例
     * */
    public static final String GET_CALL_INSTANCE = "/call/get_call_instance";

    /**
     * 指定呼叫中心实例
     * */
    public static final String GET_CALL_INSTANCE_BY_ID = "/call/get_call_instance_by_id";

    /**
     * 呼叫中心用户
     * */
    public static final String GET_CALL_USER = "/call/get_call_user";
    /**
     * 操作中心日志
     */
    public static final String  GET_ADMIN_LOG = "/admin/get_admin_log";
    /**
     * 删除操作中心日志
     */
    public static final String DELETE_ADMIN_LOG = "/admin/delete_log";
    /**
     * 删除绑定设备
     */
    public static final String DELETE_COMPUTER = "/site/delete_computer";
    /**
     * 获取管理员列表
     */
    public static final String GET_ADMIN_LIST = "/admin_auth_computer/get_list";
}
