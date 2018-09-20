package com.qiein.jupiter.constant;

import com.qiein.jupiter.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 客资状态
 *
 * @author JingChenglong 2018/04/20 18:48
 */
public class ClientStatusConst {

    /*-- 客资状态 --*/
    public static final int BE_WAIT_FILTER = 0;// 筛选中
    public static final int BE_WAIT_WAITING = 98;// 筛选后待定,
    public static final int BE_FILTER_INVALID = 99;// 筛选后无效
    public static final int BE_WAIT_MAKE_ORDER = 12;// 待接单,筛选有效
    public static final int BE_HAVE_MAKE_ORDER = 1;// 已接单-未设置，已分配，已领取，没有任何操作
    public static final int BE_ALLOTING = 999;// 未接入，已分配，未领取

    public static final int BE_TRACK = 6;// 待追踪-------未联系上
    public static final int BE_TRACK_A = 18;// 待追踪-A--72小时内再跟踪
    public static final int BE_TRACK_B = 19;// 待追踪-B--1周内再跟踪
    public static final int BE_TRACK_C = 20;// 待追踪-C--1月内再跟踪
    public static final int BE_TRACE_D = 11;// 待追踪-D--半年内再跟踪
    public static final int BE_TRACE_E = 22;// 待追踪-E--半年内再跟踪
    public static final int BE_TRACE_F = 23;// 待追踪-F--半年内再跟踪
    public static final int BE_TRACE_G = 24;// 待追踪-G--半年内再跟踪
    public static final int BE_TRACE_H = 71;// 待追踪-H--半年内再跟踪
    public static final int BE_TRACE_I = 72;// 待追踪-I--半年内再跟踪
    public static final int BE_TRACE_J = 73;// 待追踪-J--半年内再跟踪

    public static final int BE_TRACK_ZJS_A = 61;// 待追踪-A--转介绍
    public static final int BE_TRACK_ZJS_B = 62;// 待追踪-B--转介绍
    public static final int BE_TRACK_ZJS_C = 63;// 待追踪-C--转介绍
    public static final int BE_TRACE_ZJS_D = 64;// 待追踪-D--转介绍
    public static final int BE_TRACE_ZJS_E = 65;// 待追踪-E--转介绍
    public static final int BE_TRACE_ZJS_F = 66;// 待追踪-F--转介绍
    public static final int BE_TRACE_ZJS_G = 67;// 待追踪-G--转介绍
    public static final int BE_TRACE_ZJS_H = 68;// 待追踪-H--转介绍
    public static final int BE_TRACE_ZJS_I = 69;// 待追踪-I--转介绍
    public static final int BE_TRACE_ZJS_J = 70;// 待追踪-J--转介绍

    public static final int INVALID_COME = 39;// 无效到店

    public static final int NOT_COME = 50;// 未到店

    public static final int NO_ORDER = 13;// 未下单

    public static final int BE_WAITING_CALL_ZJS = 17;// 待邀约
    public static final int BE_WAITING_CALL_A = 2;// 待邀约-A------------------------有效
    public static final int BE_WAITING_CALL_B = 15;// 待邀约-B-----------------------有效
    public static final int BE_WAITING_CALL_C = 16;// 待邀约-C-----------------------有效

    public static final int BE_COMFIRM = 3;// 确定意向--------------------------------有效
    public static final int BE_HAVE_RECEPTOR = 14;// 已分配门市------------------------有效

    public static final int COME_SHOP_FAIL = 8;//邀约流失------------------有效

    public static final int INVALID_BE_CHECK = 21;// 无效待审核----------------无效
    public static final int INVALID_BE_STAY = 4;// 无效待审批----------------无效
    public static final int BE_INVALID_REJECT = 7;// 无效驳回
    public static final int BE_INVALID_ALWAYS = 80;// 永久驳回
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
//    public static final int KZ_CLASS_NEW = 1;// 新客资
//    public static final int KZ_CLASS_ORDER = 3;// 已预约
//    public static final int KZ_CLASS_TRACK = 2; //待追踪

    // 成交状态
    public static List<Integer> SUCCESS_STATUS_RANGE = new ArrayList<>(Arrays.asList(9, 30, 40));
    // 待追踪
    public static List<Integer> TRACE_STATUS_RANGE = new ArrayList<>(
            Arrays.asList(2, 6, 11, 13, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 50));

    /*-- 状态集合 --*/
    public static final String RANGE_INVALID_STATUSID = ",4,5,99,";// 无效
    public static final String RANGE_INVITE_INVALID_STATUSID = ",4,5,99,8,";// 无效
    public static final String RANGE_BE_TRACK = "2,6,11,15,16,17,18,19,20,22,23,24,25,26,27,28,29,39,61,62,63,64,65,66,67,68,69,70,71,72,73,";// 待追踪

    public static final String IS_COME_SHOP = ",9,10,30,31,32,33,34,35,36,40,41,";// 算到店
    public static final String IS_NEW_COME_SHOP = ",9,10,31,33,34,";// 算新客到店
    public static final String IS_COME_SUCCESS = ",9,30,";// 算进店成交

    public static final String IS_SUCCESS = ",9,30,31,32,40,41,";// 算成交

    public static final String IS_RUN_OFF = ",10,33,34,35,36,45,46,";// 算门店流失

    public static final String IS_BACK = ",30,35,36,";// 算回门
    public static final String IS_VALID_ZJS = ",17,2,14,15,16,3,8,9,10,30,31,32,33,34,35,36,39,40,41,45,46,";// 转介绍算有效

    //统计毛客 时用的 筛选中，筛选待定，筛选无效
    public static List<Integer> COUNT_KZ_NUM_STATUS = new ArrayList<>(Arrays.asList(99, 0, 40));


    /*-- 客资分类 --*/
    public static final int KZ_CLASS_NEW = 1;// 新客资
    public static final int KZ_CLASS_TRACK = 2;// 待跟踪
    public static final int KZ_CLASS_ORDER = 3;// 已预约
    public static final int KZ_CLASS_COME = 4;// 已进店
    public static final int KZ_CLASS_SUCCESS = 5;// 已定单
    public static final int KZ_CLASS_INVALID = 6;// 无效
    public static final int KZ_CLASS_CUSTOM1 = 7;// 自定义1
    public static final int KZ_CLASS_CUSTOM2 = 8;// 自定义2
    public static final int KZ_CLASS_CUSTOM3 = 9;// 自定义3

    public static final String KZ_CLASS_COMESHOP = ",4,5,";// 入店分类集合

    public static final String KZ_CLASS_ACTION_ALL = "all";
    public static final String KZ_CLASS_ACTION_NEW = "new";
    public static final String KZ_CLASS_ACTION_TRACE = "trace";
    public static final String KZ_CLASS_ACTION_ORDER = "order";
    public static final String KZ_CLASS_ACTION_COME = "come";
    public static final String KZ_CLASS_ACTION_SUCCESS = "success";
    public static final String KZ_CLASS_ACTION_INVALID = "invalid";
    public static final String KZ_CLASS_ACTION_CUSTOM1 = "custom1";
    public static final String KZ_CLASS_ACTION_CUSTOM2 = "custom2";
    public static final String KZ_CLASS_ACTION_CUSTOM3 = "custom3";

    public static int getClassByAction(String action) {
        if (StringUtil.isEmpty(action)) {
            return 0;
        }

        int classId = 0;
        switch (action.toLowerCase()) {
            case KZ_CLASS_ACTION_ALL:
                break;
            case KZ_CLASS_ACTION_NEW:
                classId = KZ_CLASS_NEW;
                break;
            case KZ_CLASS_ACTION_TRACE:
                classId = KZ_CLASS_TRACK;
                break;
            case KZ_CLASS_ACTION_ORDER:
                classId = KZ_CLASS_ORDER;
                break;
            case KZ_CLASS_ACTION_COME:
                classId = KZ_CLASS_COME;
                break;
            case KZ_CLASS_ACTION_SUCCESS:
                classId = KZ_CLASS_SUCCESS;
                break;
            case KZ_CLASS_ACTION_INVALID:
                classId = KZ_CLASS_INVALID;
                break;
            case KZ_CLASS_ACTION_CUSTOM1:
                classId = KZ_CLASS_CUSTOM1;
                break;
            case KZ_CLASS_ACTION_CUSTOM2:
                classId = KZ_CLASS_CUSTOM2;
                break;
            case KZ_CLASS_ACTION_CUSTOM3:
                classId = KZ_CLASS_CUSTOM3;
                break;
            default:
                break;
        }
        return classId;
    }

    public static final List<String> actionDefault = new ArrayList<>(
            Arrays.asList("new", "trace", "order", "come", "success", "invalid", "custom1", "custom2", "custom3"));
}