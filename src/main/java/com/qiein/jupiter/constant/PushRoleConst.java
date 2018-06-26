package com.qiein.jupiter.constant;

/**
 * FileName: PushRoleConst
 *
 * @author: yyx
 * @Date: 2018-6-26 17:08
 */
public class PushRoleConst {
    /**
     * 不分配-新客资系统不会自动分配，需手动转移给指定客服
     */
    public static final Integer NOT_ASSIGNED = 0;

    /**
     * 小组+员工-指定承接小组依据权重比自动分配
     */
    public static final Integer GROUP_STAFF_AVG_AUTO_ASSIGNED = 1;

    /**
     * 全部客服依据权重平均分配
     */
    public static final Integer YY_AVG_WEIGHTS_ASSIGNED = 2;

    /**
     * 部门平均+客服依据权重平均分配
     */
    public static final Integer DEPART_AVG_YY_WEIGHTS_ASSIGNED = 3;

    /**
     * 小组平均+客服依据权重平均分配
     * */
    public static final Integer GROUP_AVG_YY_WEIGHTS_ASSIGNED = 4;

    /**
     * 录入即邀约，谁录分给谁
     * */
    public static final Integer IN_YY_ASSIGNED = 5;

    /**
     * 小组+员工-指定承接小组依据权重比-跳单+领取（需客户端）
     * */
    public static final Integer GROUP_STAFF_WEIGHTS_RECEIVE = 11;

    /**
     * 全部客服依据权重平均-跳单+领取（需客户端）
     * */
    public static final Integer YY_WEIGHTS_RECEIVE = 12;

    /**
     * 部门平均+客服依据权重平均-跳单+领取（需客户端）
     * */
    public static final Integer DEPART_AVG_YY_WEIGHTS_RECEIVE =13;

    /**
     * 小组平均+客服依据权重平均-跳单+领取（需客户端）
     * */
    public static final Integer GROUP_AVG_YY_WEIGHTS_RECEIVE = 14;

}
