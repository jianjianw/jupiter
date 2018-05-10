package com.qiein.jupiter.constant;

import com.qiein.jupiter.util.StringUtil;

/**
 * 客资日志操作常量
 * 
 * @author JingChenglong 2018/05/09 17:49
 *
 */
public class ClientLogConst {

	/**
	 * 客资操作日志类型
	 */
	public static final int INFO_LOGTYPE_ADD = 1;// 新增
	public static final int INFO_LOGTYPE_EDIT = 2;// 修改
	public static final int INFO_LOGTYPE_INVITE = 3;// 客服跟进
	public static final int INFO_LOGTYPE_APPOINT = 4;// 预约进店
	public static final int INFO_LOGTYPE_SUCCESS = 5;// 订单
	public static final int INFO_LOGTYPE_MIX = 6;// 转移
	public static final int INFO_LOGTYPE_ALLOT = 7;// 分配
	public static final int INFO_LOGTYPE_REPEAT = 8;// 重复
	public static final int INFO_LOGTYPE_COMESHOP = 9;// 进店
	public static final int INFO_LOGTYPE_REMOVE = 10;// 删除
	public static final int INFO_LOGTYPE_RECEIVE = 11;// 领取

	public static final String INFO_LOG_AUTO_ALLOT_TEMPLATE = "系统自动分配该客资给 => ${groupName} 的  ${appointorname} ";
	public static final String INFO_LOG_AUTO_REVEICE_TEMPLATE = "推送客资领取消息给 => ${groupName} 的  ${appointorname} ";
	public static final String INFO_LOG_RECEIVE = "在客户端通过客资分配领取了客资";

	/**
	 * 生成客资自动分配日志
	 * 
	 * @param groupName
	 * @param appointorName
	 * @return
	 */
	public static final String getAutoAllotLog(String groupName, String appointorName) {

		if (StringUtil.isEmpty(groupName)) {
			groupName = "-";
		}
		if (StringUtil.isEmpty(appointorName)) {
			appointorName = "-";
		}

		return INFO_LOG_AUTO_ALLOT_TEMPLATE.replace("${groupName}", groupName).replace("${appointorname}",
				appointorName);
	}

	/**
	 * 生成客资自动分配日志
	 * 
	 * @param groupName
	 * @param appointorName
	 * @return
	 */
	public static final String getAutoReceiveLog(String groupName, String appointorName) {

		if (StringUtil.isEmpty(groupName)) {
			groupName = "-";
		}
		if (StringUtil.isEmpty(appointorName)) {
			appointorName = "-";
		}

		return INFO_LOG_AUTO_REVEICE_TEMPLATE.replace("${groupName}", groupName).replace("${appointorname}",
				appointorName);
	}

}