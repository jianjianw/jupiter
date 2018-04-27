package com.qiein.jupiter.exception;

/**
 * 异常枚举类
 */
public enum ExceptionEnum {
    //系统级错误
    UNKNOW_ERROR(-1, "未知错误"),
    RPC_ERROR(-2, "远程调用错误"),
    MYSQL_SQL_GRAMMAR_ERROR(-8, "sql语法错误"),
    DB_SPLIT_ERROR(-9, "系统表分割错误"),
    //token验证相关
    TOKEN_NULL(100, "token不存在"),
    TOKEN_INVALID(101, "token失效"),
    TOKEN_VERIFY_FAIL(102, "token验证失败"),
    VERIFY_PARAM_INCOMPLETE(103, "token验证参数不全"),
    VERIFY_USER_NOT_FOUND(104, "未找到验证用户信息"),
    //http请求相关
    HTTP_METHOD_NOT_SUPPORT(200, "不支持的请求方法类型"),
    HTTP_BODY_NOT_READABLE(201, "HTTP请求体无法读取"),
    HTTP_PARAMETER_ERROR(202, "HTTP请求参数无法对应"),
    //用户登录验证
    USER_NOT_FOUND(300, "用户不存在"),
    USER_IS_DEL(301, "用户已被删除"),
    USER_IS_LOCK(302, "用户已锁定"),
    USERNAME_OR_PASSWORD_ERROR(303, "用户名或密码错误"),
    VERIFY_NULL(304, "验证码不能为空"),
    VERIFY_ERROR(305, "验证码错误"),
    COMPANYID_NULL(306, "公司ID不能为空"),
    ID_NULL(307, "ID不能为空"),
    DELETE_FAIL(309, "删除失败"),
    OLD_PASSWORD_ERROR(310, "原始密码错误"),
    STAFF_EXIST_DEL(341, "该员工在离职员工中"),
    PHONE_EXIST(342, "该手机号已存在"),
    NICKNAME_EXIST(343, "该艺名已存在"),
    USERNAME_EXIST(344, "该全名已存在"),
    STAFF_ID_NULL(345, "员工ID不能为空"),
    PHONE_ERROR(346, "手机号格式错误"),
    APPOINT_NOT_FOUND(347, "邀约客服不存在"),
    //部门
    GROUP_NAME_REPEAT(401, "部门名称重复"),
    GROUP_HAVE_CHILD_GROUP(402, "该部门存在小组，请删除下属小组再进行操作"),
    GROUP_HAVE_STAFF(403, "该部门存在员工，请删除下属员工再进行操作"),
    WEIGHT_ERROR(404, "权重需在[1,20]之间"),
    ID_IS_NULL(405, "ID不能为空"),
    CHANNEL_GROUP_EXIST(406, "该渠道下客服小组已存在"),
    APPOINT_GROUP_NOT_FOUND(407, "邀约小组不存在"),
    GROUP_NOT_EXIT(408, "部门不存在"),
    //渠道&来源
    CHANNEL_NAME_REPEAT(501, "渠道名称重复"),
    CHANNEL_ID_NULL(502, "渠道编号不能为空"),
    SOURCE_ID_NULL(503, "来源编号不能为空"),
    SOURCE_NAME_REPEAT(504, "来源名称重复"),
    CHANNEL_NOT_FOUND(505, "该渠道不存在"),
    SOURCE_NOT_FOUND(506, "该来源不存在"),
    CHANNEL_HAVE_SOURCE(507, "该渠道下存在来源，请先删除下属所有来源再继续操作"),
    //拍摄地
    SHOP_EXIST(601, "拍摄地已存在"),
    SHOP_ID_NULL(602, "拍摄地ID不能为空"),
    SHOP_NOT_FOUND(603, "拍摄地不存在"),
    //权限
    ROLE_EXIST(701, "该角权限已存在"),
    ROLE_EDIT_FAIL(702, "权限修改失败"),
    ROLE_DELETE_ERROR(703, "该权限有绑定人员，无法删除"),
    //企业配置
    MENU_NULL(801, "企业菜单为空"),
    //角色
    ROLE_ERROR(901, "角色错误"),
    //品牌
    BRAND_NAME_REPEAT(1001, "该品牌已存在"),
    BRAND_NOT_FOUND(1002, "该品牌不存在"),
    BRAND_ID_NULL(1003, "品牌编号不能为空"),
    BRAND_HAVE_CHANNEL(1004, "该品牌下存在渠道，请先删除下属渠道再继续操作");

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
