package com.qiein.jupiter.msg.goeasy;

/**
 * 消息信息常量类
 * 
 * @author JingChenglong 2018/04/17 16:10
 *
 */
public class MessageConts {

	// WEB消息类型
	public static final String MSG_TYPE_COMMON = "common";// 通用消息
	public static final String MSG_TYPE_SUCCESS = "success";// 成功类型消息
	public static final String MSG_TYPE_WARN = "warn";// 警示类型消息
	public static final String MSG_TYPE_ERROR = "error";// 错误类型消息
	public static final String MSG_TYPE_FLASH = "flash";// 闪信
	public static final String MSG_TYPE_RECEIVE = "receive";// 客资领取
	public static final String MSG_TYPE_INFO_REFRESH = "info_refresh";// 客资重载消息
	public static final String MSG_TYPE_STAFF_REFRESH = "staff_refresh";// 身份重验证消息
	public static final String MSG_TYPE_STATUS_REFRESH = "status_refresh";// 状态重验证消息

	// 客户端消息类型
	public static final String MSG_APP_INFO_REVEIVE = "receive";// 客资领取
}