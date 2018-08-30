package com.qiein.jupiter.msg.websocket;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.enums.WebSocketMsgEnum;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.WebSocketMsgDTO;
import com.qiein.jupiter.web.entity.dto.OrderSuccessMsg;
import com.qiein.jupiter.web.service.ApolloService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ApolloService apolloService;

    /**
     * 构造
     *
     * @param msgUrl
     */
    public WebSocketMsgUtil(@Value("${apollo.webSocket.msgUrl}") String msgUrl) {
        this.msgUrl = msgUrl;
    }

    /**
     * 异步提交一个post msg
     *
     * @param msg
     */
    private void sendMsgAsync(String msg) {
        HttpClient.textBody(msgUrl)
                // post提交json
                .json(msg).execute();
//        apolloService.postWebSocketMsg(msg);
    }

    // /**
    // * 同步提交一个请求
    // *
    // * @param msg
    // */
    // private void sendMsgSync(String msg) {
    // HttpClient.post(msgUrl)
    // // post提交json
    // .param("msg", msg)
    // .asString();
    // }

    /**
     * 发送企业消息
     *
     * @param msgDTO
     */
    private void sendMsg(WebSocketMsgDTO msgDTO) {
        this.sendMsgAsync(JSONObject.toJSONString(msgDTO));
    }

    /**
     * 推送成功订单的消息
     */
    public void pushOrderSuccessMsg(OrderSuccessMsg orderSuccessMsg) {
        WebSocketMsgDTO msgDTO = new WebSocketMsgDTO();
        msgDTO.setType(WebSocketMsgEnum.OrderSuccess);
        msgDTO.setCompanyId(orderSuccessMsg.getCompanyId());
        msgDTO.setContent(JSONObject.toJSONString(orderSuccessMsg));
        this.sendMsg(msgDTO);
    }

    /**
     * 推送全员刷新的消息
     */
    public void pushAllRefreshMsg(int companyId) {
        WebSocketMsgDTO msgDTO = new WebSocketMsgDTO();
        msgDTO.setType(WebSocketMsgEnum.AllRefresh);
        msgDTO.setCompanyId(companyId);
        msgDTO.setContent("");
        this.sendMsg(msgDTO);
    }

    /**
     * 推送 刷新基础信息
     */
    public void pushBaseInfoFresh(int companyId, Integer staffId) {
        WebSocketMsgDTO msgDTO = new WebSocketMsgDTO();
        msgDTO.setType(WebSocketMsgEnum.BaseInfoRefresh);
        msgDTO.setCompanyId(companyId);
        if (staffId != null && staffId != 0) {
            msgDTO.setStaffId(staffId);
        }
        msgDTO.setContent("");
        this.sendMsg(msgDTO);
    }

    /**
     * 推送 信息
     */
    public void pushAlertMsg(int companyId, Integer staffId, String content) {
        WebSocketMsgDTO msgDTO = new WebSocketMsgDTO();
        msgDTO.setType(WebSocketMsgEnum.AlertMsg);
        msgDTO.setCompanyId(companyId);
        if (staffId != null && staffId != 0) {
            msgDTO.setStaffId(staffId);
        }
        msgDTO.setContent(content);
        this.sendMsg(msgDTO);
    }

    /**
     * 推送领取客资的消息
     */
    public void pushReceiveClient(int companyId, int staffId, String head, ClientGoEasyDTO info) {
        WebSocketMsgDTO msgDTO = new WebSocketMsgDTO();
        msgDTO.setType(WebSocketMsgEnum.ReceiveClient);
        msgDTO.setCompanyId(companyId);
        msgDTO.setStaffId(staffId);
        JSONObject contentJson = new JSONObject();
        contentJson.put("head", head);
        contentJson.put("kz", info);
        contentJson.put("contact", StringUtil.isNotEmpty(info.getKzPhone()) ? info.getKzPhone() :
                StringUtil.isNotEmpty(info.getKzWechat()) ? info.getKzWechat() :
                        StringUtil.isNotEmpty(info.getKzQq()) ? info.getKzQq() : info.getKzWw());
        msgDTO.setContent(contentJson.toString());
        this.sendMsg(msgDTO);
    }

    /**
     * 发送客资重载
     */
    public void pushClientInfoRefresh(int companyId, Integer staffId) {
        WebSocketMsgDTO msgDTO = new WebSocketMsgDTO();
        msgDTO.setType(WebSocketMsgEnum.ClientInfoRefresh);
        msgDTO.setCompanyId(companyId);
        if (staffId != null && staffId != 0) {
            msgDTO.setStaffId(staffId);
        }
        this.sendMsg(msgDTO);
    }


}
