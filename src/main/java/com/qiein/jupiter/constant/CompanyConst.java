package com.qiein.jupiter.constant;

/**
 * @Author: shiTao
 */
public class CompanyConst {
    /**
     * 数据库字段
     */
    public static final String COLUMN_UNABLEINVALIDRANGE = "UNABLEINVALIDRANGE";// 电商客资超过指定时间不能返无效
    public static final String COLUMN_UNABLEAPPOINTOR = "UNABLEAPPOINTOR";// 电商客资录入时不能直接指定客服
    public static final String COLUMN_UNABLESELFLINE = "UNABLESELFLINE";// 员工个人不能自己操作上线下线
    public static final String COLUMN_NOTSELFBLIND = "NOTSELFBLIND";// 非自己客资脱敏显示
    public static final String COLUMN_KZINTERVAL = "KZINTERVAL";// 客服领取客资多少时间后不能领取下一个
    public static final String COLUMN_LIMITDEFAULT = "LIMITDEFAULT";// 客服日领单默认最单限额
    public static final String COLUMN_OVERTIME = "OVERTIME";// 客服领取客资超时时间
    public static final String COLUMN_SSOLIMIT = "SSOLIMIT";// 是否允许单账号同时多端在线
}
