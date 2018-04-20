package com.qiein.jupiter.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 渠道常量
 * Created by Administrator on 2018/4/19 0019.
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
}