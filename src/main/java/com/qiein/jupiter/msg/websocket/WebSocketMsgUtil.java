package com.qiein.jupiter.msg.websocket;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.web.entity.dto.CompanyMsgDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMsgUtil {
    @Value("${webSocket.msgUrl}")
    private String msgUrl;

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
