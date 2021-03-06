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
    METHOD_ARGUMENT_TYPE_MISMATCH_ERROR(-10, "方法参数无法转换"),
    CAN_NOT_FIND_USER_FROM_REQ(-11, "未能从请求中获取到用户信息"),
    APOLLO_URL_NOT_SET(-12, "阿波罗地址未设置"),
    REMOTE_CALL_ERROR(-13, "远程调用错误"),
    //常用错误
    ADD_FAIL(1, "新增失败"),
    LOSE_FILED(2, "缺少必须条件"),
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
    //用户验证
    USER_NOT_FOUND(300, "用户不存在"),
    USER_IS_DEL(301, "用户已被删除"),
    USER_IS_LOCK(302, "用户已锁定"),
    USERNAME_OR_PASSWORD_ERROR(303, "用户名或密码错误"),
    VERIFY_NULL(304, "验证码不能为空"),
    VERIFY_ERROR(305, "验证码错误"),
    COMPANYID_NULL(306, "公司ID不能为空"),
    ID_NULL(307, "ID不能为空"),
    DELETE_FAIL(309, "删除失败"),
    EDIT_FAIL(310, "编辑失败"),
    OLD_PASSWORD_ERROR(310, "原始密码错误"),
    STAFF_EXIST_DEL(341, "该员工在离职员工中"),
    PHONE_EXIST(342, "该手机号已存在"),
    NICKNAME_EXIST(343, "该艺名已存在"),
    USERNAME_EXIST(344, "该全名已存在"),
    STAFF_ID_NULL(345, "员工ID不能为空"),
    PHONE_ERROR(346, "手机号格式错误"),
    APPOINT_NOT_FOUND(347, "邀约客服不存在"),
    NICKNAME_IS_NULL(348, "艺名不能为空"),
    USERNAME_IS_NULL(349, "全名不能为空"),
    PHONE_IS_NULL(350, "手机号不能为空"),
    STAFF_IS_STOP_RECEIPT(351, "员工已被停单，请联系主管解决"),
    STAFF_IS_LIMIT(352, "员工已达到接单限额，请联系主管解决"),
    STAFF_STATUS_UPDATE_FAIL(353, "员工状态修改失败"),
    STAFF_IS_NOT_EXIST(354, "员工不存在"),
    SIMPLE_PASSWORD(355, "密码不能低于6为位"),
    SIMPLE_NUMBER_PASSWORD(356, "密码不能为纯数字"),
    PASSWORD_NULL(357, "密码不能为空"),
    STAFF_CAN_NOT_DEL(358, "该员工存在有客资，请交接后再删除"),
    ONLY_APP_LOGIN(359, "只允许客户端登录"),
    COLLECTOR_NOT_FOUND(360, "提报人不存在"),
    OLD_CLIENT_PHONE_IS_NOT_LEGAL(361, "请检查老客手机号是否正确，手机号的格式为11位纯数字"),
    //部门
    GROUP_NAME_REPEAT(401, "部门名称重复"),
    GROUP_HAVE_CHILD_GROUP(402, "该部门存在小组，请删除下属小组再进行操作"),
    GROUP_HAVE_STAFF(403, "该部门存在员工，请删除下属员工再进行操作"),
    WEIGHT_ERROR(404, "权重需在[1,20]之间"),
    ID_IS_NULL(405, "ID不能为空"),
    CHANNEL_GROUP_EXIST(406, "该渠道下客服小组已存在"),
    APPOINT_GROUP_NOT_FOUND(407, "邀约小组不存在"),
    GROUP_NOT_EXIT(408, "部门不存在"),
    GROUP_IS_NULL(409, "小组不能为空"),
    GROUP_STAFF_EXISTS(410, "小组员工已经存在"),
    GROUP_STAFF_NOT_EXISTS(411, "小组员工不存在"),
    //渠道&来源
    CHANNEL_NAME_REPEAT(501, "渠道名称重复"),
    CHANNEL_ID_NULL(502, "渠道编号不能为空"),
    SOURCE_ID_NULL(503, "来源编号不能为空"),
    SOURCE_NAME_REPEAT(504, "来源名称重复"),
    CHANNEL_NOT_FOUND(505, "该渠道不存在或已关闭"),
    SOURCE_NOT_FOUND(506, "该来源不存在或已关闭"),
    CHANNEL_HAVE_SOURCE(507, "该渠道下存在来源，请先删除下属所有来源再继续操作"),
    SOURCE_HAVE_KZ(508, "该来源下存在客资，不可删除"),
    YYID_NOT_EXISTS(509, "邀约人员不存在"),
    SHOP_SOURCE_ERROR(510, "门店自然入客渠道配置错误，请联系管理员"),
    //拍摄地
    SHOP_EXIST(601, "门店已存在"),
    SHOP_ID_NULL(602, "门店ID不能为空"),
    SHOP_NOT_FOUND(603, "门店不存在"),
    SHOP_KZ_EXIST(604, "该门店存在客资，不能删除"),
    //权限
    ROLE_EXIST(701, "该角权限已存在"),
    ROLE_EDIT_FAIL(702, "权限修改失败"),
    ROLE_DELETE_ERROR(703, "该权限有绑定人员，无法删除"),
    ROLE_IS_NULL(704, "权限不能为空"),
    //企业配置
    MENU_NULL(801, "企业菜单为空"),
    REPEAT_TIME_ERRPR(802, "时间类型，天数都不能为空"),
    //角色
    ROLE_ERROR(901, "角色错误"),
    ROLE_ID_IS_NULL(902, "角色ID不能为空"),
    //品牌
    BRAND_NAME_REPEAT(1001, "该品牌已存在"),
    BRAND_NOT_FOUND(1002, "该品牌不存在"),
    BRAND_ID_NULL(1003, "品牌编号不能为空"),
    BRAND_HAVE_CHANNEL(1004, "品牌下存在渠道，请先删除下属渠道再继续操作"),
    //无效原因
    INVALID_REASON_EXIST(1101, "无效原因已存在"),
    INVALID_REASON_TYPE_NULL(1102, "类型不能为空"),
    //公司
    COMPANY_ID_NULL(1201, "该公司编号不能为空"),
    COMPANY_IS_LOCK(1202, "该公司已经被锁定，请联系平台管理员！"),
    COMPANY_EDIT_ZJS_SET(1203, "编辑公司转介绍设置"),
    //导入导出
    EXCEL_IS_NULL(1301, "Excel文件是空的"),
    EXCEL_ADD_FAIL(1302, "Excele添加数据库失败"),
    BATCH_ADD_NULL(1303, "导入内容不能为空"),
    TIME_ERROR(1304,"提报时间格式错误"),
    APPOINT_TIME_ERROR(1305,"预约进店时间格式错误"),
    COME_SHOP_TIME_ERROR(1306,"入店时间格式错误"),
    SUCCESS_TIME_ERROR(1307,"成交时间格式错误"),
    KZ_NAME_LENGTH_TOO_LONG(1308,"客资姓名过长"),
    KZ_PHONE_LENGTH_TOO_LONG(1309,"客资电话过长"),
    KZ_WECHAT_LENGTH_TOO_LONG(1310,"客资微信过长"),
    KZ_QQ_LENGTH_TOO_LONG(1311,"客资QQ过长"),
    COLLECTOR_NAME_LENGTH_TOO_LONG(1312,"客资录入人姓名过长"),
    APPOINT_NAME_LENGTH_TOO_LONG(1313,"邀约姓名过长"),
    SHOP_NAME_LENGTH_TOO_LONG(1314,"门店名称过长"),
    MEMO_LENGTH_TOO_LONG(1314,"备注过长"),
    OLD_KZ_NAME_LENGTH_TOO_LONG(1315,"老客姓名过长"),
    OLD_KZ_PHONE_LENGTH_TOO_LONG(1316,"老客电话过长"),
    //客资
    KZ_CONTACT_INFORMATION(1401, "客资联系方式不能为空"),
    KZ_ADD_FAIL(1402, "录入失败"),
    KZ_ID_IS_NULL(1403, "客资ID不能为空"),
    TYPEID_IS_NULL(1404, "拍摄类型不能为空"),
    KZ_EDIT_FAIL(1405, "客资编辑失败"),
    APPROVAL_RST_IS_NULL(1406, "审批结果不能为空"),
    APPROVAL_MEMO_IS_NULL(1407, "请填写审批备注"),
    SHOP_ID_IS_NULL(1408, "门店ID不能为空"),
    APPOINT_TIME_IS_NULL(1409, "预约时间不能为空"),
    AMOUNT_IS_NULL(1410, "套餐金额不能为空"),
    STAY_AMOUNT_IS_NULL(1411, "收款金额不能为空"),
    SUCCESS_TIME_IS_NULL(1412, "订单时间不能为空"),
    STAY_TIME_IS_NULL(1413, "收款时间不能为空"),
    JD_RESULT_IS_NULL(1414, "接待结果不能为空"),
    IS_NOT_KZ_PHONE_OR_WECHAT(1415, "请输入正确的客资手机号或微信"),
    AMOUNT_ERROR(1416, "收款总价不能大于套系金额"),
    KZ_CONTACT_WAY_WRITE_ONE(1417,"请填写至少一个联系方式"),
    ALREADY_SK(1418,"该客资已筛选，无法二次筛选"),
    //OSS对象存储
    OSS_UPLOAD_TYPE_ERROR(1501, "上传类型错误"),
    OSS_UPLOAD_SIZE_ERROR(1502, "图片大小超过3M"),
    OSS_UPLOAD_FAIL(1503, "图片上传失败"),
    OSS_NO_FILE(1504, "未选择文件"),
    //客资分配
    INFO_ERROR(1601, "客资信息错误"),
    INFO_OVERTIME_ERROR(1602, "客资领取超时"),
    INFO_OTHER_APPOINTOR(1603, "该客资已分配给其他客服"),
    INFO_BE_RECEIVED(1604, "该客资已被领取"),
    INFO_IS_NULL(1605, "客资信息不能为空"),
    INFO_STATUS_EDIT_ERROR(1606, "客资状态修改错误"),
    INFO_EDIT_ERROR(1607, "客资信息修改错误"),
    LOG_ERROR(1608, "客资日志记录错误"),
    ALLOT_LOG_ERROR(1609, "客资分配日志记录错误"),
    STAFF_EDIT_ERROR(1610, "分配流程员工信息标注错误"),
    ALLOT_ERROR(1611, "请选择要分配的客资和客服"),
    ALLOTED_ERROR(1612, "所选客资无法进行分配"),
    APPOINTOR_ERROR(1613, "客服信息获取失败"),
    KZ_STATUS_ERROR(1614, "该客资无法标记为未到店"),
    //状态
    STS_COLUMN_ERROR(1701, "状态颜色表示错误"),
    STS_DEFAULT_ERROR(1702, "默认颜色信息缺失"),
    STS_GET_ERROR(1703, "企业状态信息获取错误"),
    //ip白名单
    IP_ERROR(1801, "IP输入有误"),
    IP_UNALLOW(1802, "IP无权访问"),
    IP_NOT_IN_SAFETY(1803, "当前IP不在安全IP范围内，请联系主管！"),
    //apollo
    ACCESSKEY_ERROR(1901, "accesskey错误"),
    SIGN_ERROR(1902, "请求签名错误"),
    //字典
    DICTNAME_EXIST(2001, "字典名称重复"),
    //微信
    WX_BIND_ERROR(2101, "微信绑定失败"),
    WX_REMOVE_BIND_FAIL(2102, "微信解绑失败"),
    WX_NOT_BIND(2103, "微信未绑定"),
    GET_WX_INFO_FAIL(2104, "获取用户微信信息失败"),
    //金数据
    FORM_WAS_IN(2201, "表单已存在"),
    FORM_NOT_EXISTS(2202, "表单不存在"),
    GOLD_DATA_ARR_LENGTH_ERROR(2203, "数组数据错误"),
    ZX_STYLE_NOT_EXISTS(2204, "咨询类型不存在"),
    //分页
    PAGESIZE_MAX_SIZE_ERROR(2301, "每页最多500条"),
    //短信
    TEMPLATE_LOSE(2401, "短信模板获取失败"),
    TEMPLATE_NOT_IN(2402, "短信模板不存在"),
    CANT_EDIT_HJ(2403,"合计数据不能直接修改"),
    //报表
    START_TIME_OR_END_TIME_IS_NULL(2501, "开始时间或者结束时间不能为空"),
    SEARCH_TYPE_IS_UNKNOW(2502, "请选择查询一个已知的数据类型"),
    SEARCH_CLIENT_TYPE_IS_UNKNOW(2503, "请选择查询一个已知的客资类型"),
    //外呼
    NOT_FOUND_BIND_USER(2601, "未找到绑定用户信息"),
    CALLER_OR_CALLEE_IS_NULL(2602, "主叫或者被叫电话为空"),
    CALL_ID_IS_NULL(2603, "实例ID不能为空"),
    CALL_CONSUMER_PHONE_IS_NULL(2604, "客服电话不能为空"),
    CALL_CONSUMER_IS_EXISTS(2605, "客服已经存在"),
    CALL_CONSUMER_ID_IS_NULL(2606, "客服id为空"),
    //黑名单
    BLACK_WAS_IN(2701, "该手机号已存在"),
    KZ_IN_BLACK_LIST(2702, "该客资已被添加到黑名单"),

    //短信
    CANT_FIND_TEMPLATE(2801,"未申请短信模板");

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