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
    /**
     * 删除管理员
     */
    public static final String DELETE_ADMIN = "/admin_auth_computer/delete_admin";
    /**
     * 添加场地
     * */
    public static final String ADD_SITE = "/site/add";

    /**
     * 编辑场地
     * */
    public static final String EDIT_SITE = "/site/edit";

    /**
     * 删除场地
     * */
    public static final String DEL_SITE = "/site/del";

    /**
     * 场地列表
     * */
    public static final String SITE_COMPUTER_LIST = "/site/list";
    /**
     * 场地列表
     * */
    public static final String SITE_LIST = "/site/list";

    /**
     * 场地电脑总数及周增加数
     * */
    public static final String COMPUTER_COUNT = "/site/get_computer_count";

}
