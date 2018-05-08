package com.qiein.jupiter.constant;

/**
 * 客资状态
 * 
 * @author JingChenglong 2018/04/20 18:48
 *
 */
public class ClientStatusConst {

	/*-- 客资状态 --*/
	public static final int BE_WAIT_FILTER = 0;// 筛选中
	public static final int BE_WAIT_WAITING = 98;// 筛选待定
	public static final int BE_FILTER_INVALID = 99;// 筛选后无效
	public static final int BE_WAIT_MAKE_ORDER = 12;// 待接单
	public static final int BE_HAVE_MAKE_ORDER = 1;// 已接单
	public static final int BE_ALLOTING = 999;// 未接入

	public static final int BE_TRACK = 6;// 待追踪-------未联系上
	public static final int BE_TRACK_A = 18;// 待追踪-A--72小时内再跟踪
	public static final int BE_TRACK_B = 19;// 待追踪-B--1周内再跟踪
	public static final int BE_TRACK_C = 20;// 待追踪-C--1月内再跟踪
	public static final int BE_TRACE_D = 11;// 待追踪-D--半年内再跟踪
	public static final int BE_TRACE_E = 22;// 待追踪-E--半年内再跟踪
	public static final int BE_TRACE_F = 23;// 待追踪-F--半年内再跟踪
	public static final int BE_TRACE_G = 24;// 待追踪-G--半年内再跟踪

	public static final int INVALID_COME = 39;// 无效到店

	public static final int NOT_COME = 50;// 未到店

	public static final int NO_ORDER = 13;// 未下单

	public static final int BE_WAITING_CALL_ZJS = 17;// 待邀约
	public static final int BE_WAITING_CALL_A = 2;// 待邀约-A------------------------有效
	public static final int BE_WAITING_CALL_B = 15;// 待邀约-B-----------------------有效
	public static final int BE_WAITING_CALL_C = 16;// 待邀约-C-----------------------有效

	public static final int BE_COMFIRM = 3;// 确定意向--------------------------------有效
	public static final int BE_HAVE_RECEPTOR = 14;// 已分配门市------------------------有效

	public static final int COME_SHOP_FAIL = 8;// 未到店/定别家/邀约流失------------------有效

	public static final int INVALID_BE_CHECK = 21;// 无效待审核----------------无效
	public static final int INVALID_BE_STAY = 4;// 无效待审批----------------无效
	public static final int BE_INVALID_REJECT = 7;// 无效驳回
	public static final int BE_INVALID = 5;// 无效--------------------------无效

	/*-- 算到店 --*/
	public static final int BE_SUCCESS = 9;// 成功成交（顺利订单）------------------------有效
	public static final int BE_SUCCESS_STAY = 33;// 门店保留
	public static final int BE_RUN_OFF = 10;// 流失----------------------------------有效
	public static final int BACK_RUN_OFF = 36;// 回单流失
	public static final int BACK_SHOP_SUCCESS = 30;// 回单成交
	public static final int BACK_SUCCESS_SHOP_STAY = 35;// 回单保留
	public static final int SHOP_STAY_CHECK_GIFT = 34;// 核销已到店

	public static final int ONLINE_SUCCESS = 40;// 线上成交
	public static final int ONLINE_STAY = 28;// 线上保留

	/*-- 客资分类 --*/
	public static final int KZ_CLASS_NEW = 1;// 新客资
	public static final int KZ_CLASS_TRACE = 2;// 待跟踪
	public static final int KZ_CLASS_ORDER = 3;// 已预约
	public static final int KZ_CLASS_COME = 4;// 已进店
	public static final int KZ_CLASS_SUCCESS = 5;// 已定单
	public static final int KZ_CLASS_INVALID = 6;// 无效
	public static final int KZ_CLASS_CUSTOM1 = 7;// 自定义1
	public static final int KZ_CLASS_CUSTOM2 = 8;// 自定义2
	public static final int KZ_CLASS_CUSTOM3 = 9;// 自定义3
}