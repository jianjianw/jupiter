package com.qiein.jupiter.exception;

/**
 * 异常枚举类
 */
public enum ExceptionEnum {
    //系统级错误
    UNKNOW_ERROR(-1, "未知错误"),
    MYSQL_SQL_GRAMMAR_ERROR(-8, "sql语法错误"),
    //token验证相关
    TOKEN_NULL(100, "token不存在"),
    TOKEN_INVALID(101, "token失效"),
    TOKEN_VERIFY_FAIL(102, "token验证失败"),
    VERIFY_PARAM_INCOMPLETE(103, "token验证参数不全"),
    VERIFY_USER_NOT_FOUND(104, "未找到验证用户信息"),
    //http请求相关
    HTTP_METHOD_NOT_SUPPORT(200, "不支持的请求方法类型"),
    HTTP_BODY_NOT_READABLE(201, "http请求体无法读取"),
    //用户登录验证
    USER_NOT_FOUND(300, "用户不存在"),
    USER_IS_DEL(301, "用户已被删除"),
    USER_IS_LOCK(302, "用户已锁定"),
    USERNAME_OR_PASSWORD_ERROR(303, "用户名或密码错误"),
    VERIFY_NULL(304, "验证码不能为空"),
    VERIFY_ERROR(305, "验证码错误"),
    COMPANYID_NULL(306, "公司ID不能为空"),
    ID_NULL(307, "id不能为空"),
    ROLE_EXIST(308, "该角色已存在"),
    DELETE_FAIL(309, "删除失败"),
    ROLE_EDIT_FAIL(340, "角色修改失败"),
    STAFF_EXIST_DEL(341, "该员工在离职员工中"),
    PHONE_EXIST(342, "该手机号已存在"),
    NICKNAME_EXIST(343, "该艺名已存在"),
    USERNAME_EXIST(344, "该全名已存在"),
    STAFF_ID_NULL(345, "员工ID不能为空"),
    //部门
    GROUP_NAME_REPEAT(401,"部门名称重复"),
    GROUP_HAVE_CHILD_GROUP(402,"该部门存在小组，请删除下属小组再进行操作"),
    GROUP_HAVE_STAFF(403,"该部门存在员工，请删除下属员工再进行操作");

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
