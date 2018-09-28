package com.qiein.jupiter.web.entity.dto;

/**
 * 员工消息设置
 *
 * @Author: shiTao
 */
public class StaffMsgSetDTO {

    /**
     * 订单爆彩
     */
    private boolean allowOrderBullyScreen;

    /**
     * 无效提醒
     */
    private boolean allowWxDingMsg;



    public boolean isAllowOrderBullyScreen() {
        return allowOrderBullyScreen;
    }

    public void setAllowOrderBullyScreen(boolean allowOrderBullyScreen) {
        this.allowOrderBullyScreen = allowOrderBullyScreen;
    }

    public boolean isAllowWxDingMsg() {
        return allowWxDingMsg;
    }

    public void setAllowWxDingMsg(boolean allowWxDingMsg) {
        this.allowWxDingMsg = allowWxDingMsg;
    }
}
