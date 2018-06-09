package com.qiein.jupiter.enums;


/**
 * websocket消息类型
 */
public enum WebSocketMsgEnum {
    //订单成功
    OrderSuccess(1),
    //通用
    Common(2),
    //闪屏
    Flash(3),
    //微信刷新
    WeChatCallBack(4);

    /**
     * 类型
     */
    private int type;


    /**
     * 私有构造,防止被外部调用
     */
    WebSocketMsgEnum(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.type);
    }

}
