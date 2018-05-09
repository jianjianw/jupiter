package com.qiein.jupiter.constant;

/**
 * 客资信息常量
 * 
 * @author JingChenglong 2018/05/09 11:45
 *
 */
public class ClientConst {
	/**
	 * 客资分配操作类型
	 */
	public static final int ALLOT_SYSTEM_AUTO = 1;// 系统自动分配
	public static final int ALLOT_HANDLER = 2;// 手动分配
	public static final int ALLOT_WHILE_COLLECT = 3;// 录入时指定
	public static final int ALLOT_BOSS_MIX = 4;// 主管转移
	public static final int ALLOT_EXCEL = 5; // Excel导入

	/**
	 * 客资分配日志记录状态
	 */
	public static final int ALLOT_LOG_STATUS_NO = 0;// 未领取
	public static final int ALLOT_LOG_STATUS_YES = 1;// 已领取
	public static final int ALLOT_LOG_STATUS_REFUSE = 2;// 已拒绝

	/**
	 * 查看客资范围
	 */
	public static final int PMS_LOMIT_SELEF = 1;// 只看自己
	public static final int PMS_LOMIT_GROUP = 2;// 只看本组
	public static final int PMS_LOMIT_DEPRT = 3;// 只看部门
	public static final int PMS_LOMIT_SHOP = 4;// 只看本店
	public static final int PMS_LOMIT_ALL = 0;// 查看所有
}