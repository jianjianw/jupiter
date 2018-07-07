package com.qiein.jupiter.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 渠道常量
 * Created by Tt(叶华葳)
 * on 2018/4/19 0019.
 */
public class ChannelConstant {

    // 全部渠道常量列表
    public static final List<Integer> ALL_TYPE_LIST = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    // 电商渠道常量列表
    public static final List<Integer> DS_TYPE_LIST = new ArrayList<Integer>(Arrays.asList(1, 2));
    // 转介绍渠道常量列表
    public static final List<Integer> ZJS_TYPE_LIST = new ArrayList<Integer>(Arrays.asList(3, 4, 5));
    // 自然渠道常量列表
    public static final List<Integer> ZR_TYPE_LIST = new ArrayList<Integer>(Arrays.asList(6, 7));

    // 纯电商
    public static final int DS_ONLY = 1;
    // 电商转介绍
    public static final int DS_ZJS = 2;
    // 员工转介绍
    public static final int STAFF_ZJS = 3;
    // 门市指名转介绍
    public static final int STAFF_MS = 4;
    // 外部转介绍
    public static final int STAFF_OUT = 5;
    // 门店自然入客
    public static final int SHOP_NATURAL = 6;
    // 门店外展
    public static final int SHOP_EXHIBITION = 7;

    // 0：不分配-新客资系统不会自动分配，需手动转移给指定客服
    public static final int PUSH_RULE_NULL = 0;
    // 1：小组+员工-指定承接小组依据权重比自动分配
    public static final int PUSH_RULE_AVG_ALLOT = 1;
    // 5：录入即邀约，谁录分给谁
    public static final int PUSH_RULE_SELF = 5;
    // 11：小组+员工-指定承接小组依据权重比-跳单+领取（需客户端）
    public static final int PUSH_RULE_AVG_RECEIVE = 11;
    //12.指定客服
    public static final int PUSH_RULE_ASSIGN_APPOINT = 12;
    //13:自由领取
    public static final int PUSH_RULE_EVERYONE_CAN_GET = 13;
    //14：小组平均
    public static final int PUSH_RULE_GROUP_AVG = 14;
}