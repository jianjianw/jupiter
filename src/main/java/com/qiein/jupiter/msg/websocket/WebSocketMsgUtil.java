package com.qiein.jupiter.msg.websocket;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.web.entity.dto.CompanyMsgDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * websocket消息推送工具
 */
@Component
public class WebSocketMsgUtil {
    /**
     * 消息推送地址
     */
    private final String msgUrl;

    /**
     * 构造
     *
     * @param msgUrl
     */
    public WebSocketMsgUtil(@Value("${webSocket.msgUrl}") String msgUrl) {
        this.msgUrl = msgUrl;
    }

    /**
     * 异步提交一个post msg
     *
     * @param msg
     */
    private void sendMsgAsync(String msg) {
        HttpClient.post(msgUrl)
                // post提交json
                .param("msg", msg)
                .execute();
    }

    /**
     * 同步提交一个请求
     *
     * @param msg
     */
    private void sendMsgSync(String msg) {
        HttpClient.post(msgUrl)
                // post提交json
                .param("msg", msg)
                .asString();
    }

    /**
     * 发送企业消息
     *
     * @param msgDTO
     */
    public void sendMsg(CompanyMsgDTO msgDTO) {
        this.sendMsgAsync(JSONObject.toJSONString(msgDTO));
    }


}
