package com.qiein.jupiter.enums;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 提示消息枚举类
 */
@JSONType(serializeEnumAsJavaBean = true)
public enum TigMsgEnum {
	/**
	 * 成功
	 */
	SUCCESS("成功"),

	/**
	 * 失败
	 */
	FAIL("失败"),

	/**
	 * 错误
	 */
	ERROR("错误"),

	/**
	 * 保存成功
	 */
	SAVE_SUCCESS("保存成功"),
	/**
	 * 修改成功
	 */
	UPDATE_SUCCESS("修改成功"),
	/**
	 * 删除成功
	 */
	DELETE_SUCCESS("删除成功"),

	/**
	 * 用户名不能为空
	 */
	USER_NAME_NOT_NULL("用户名不能为空"),

	/**
	 * 密码不能为空
	 */
	PASS_WORD_NOT_NULL("密码不能为空"),

	/**
	 * 艺名不能为空
	 */
	NICK_NAME_NULL("艺名不能为空"),
	/**
	 * 添加角色成功
	 */
	ADD_ROLE_SUCCESS("添加角色成功"),
	/**
	 * 删除角色成功
	 */
	DELETE_ROLE_SUCCESS("删除角色成功"),

	/**
	 * 修改角色成功
	 */
	EDIT_ROLE_SUCCESS("修改角色成功"),

	/**
	 * 修改成功
	 */
	EDIT_SUCCESS("修改成功"),

	/**
	 * 添加渠道成功
	 */
	ADD_CHANNEL_SUCCESS("添加渠道成功"),

	/**
	 * 删除渠道成功
	 */
	DEL_CHANNEL_SUCCESS("删除渠道成功"),

	/**
	 * 编辑渠道成功
	 */
	EDIT_CHANNEL_SUCCESS("编辑渠道成功"),

	/**
	 * 渠道重名
	 */
	CHANNEL_NAME_ERROR("渠道重名"),

	/**
	 * 添加来源成功
	 */
	ADD_SOURCE_SUCCESS("添加来源成功"),

	/**
	 * 删除来源成功
	 */
	DEL_SOURCE_SUCCESS("删除来源成功"),

	/**
	 * 编辑来源成功
	 */
	EDIT_SOURCE_SUCCESS("编辑来源成功"),

	/**
	 * 添加品牌成功
	 */
	ADD_BRAND_SUCCESS("添加品牌成功"),

	/**
	 * 恢复离职员工成功
	 */
	RESOTRE_SUCCESS("恢复成功"),

	/**
	 * 操作成功
	 */
	OPERATE_SUCCESS("操作成功"),

	/**
	 * 导入成功
	 */
	IMPORT_SUCCESS("导入成功"),
	/**
	 * 上线成功
	 */
	ONLINE_SUCCESS("上线成功"),
	/**
	 * 下线成功
	 */
	OFFLINE_SUCCESS("下线成功"),

	/**
	 * 客资领取成功
	 */
	INFO_RECEIVE_SUCCESS("客资领取成功"),

	/**
	 * 客资拒接成功
	 */
	INFO_REFUSE_SUCCESS("客资拒接成功");

	private String desc;

	TigMsgEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return desc;
	}
}
