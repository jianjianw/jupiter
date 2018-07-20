package com.qiein.jupiter.web.entity.dto;

/**
 * 钉钉企业消息DTO
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/13 10:17
 */
public class SendDingMsgDTO {
    private String corpId;
    private Long agentId;//微应用Id
    /**
     * 钉钉可发送的消息类型
     * 可以看这个网站：
     *  https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.8q4q4h&treeId=385&articleId=104972&docType=1#s0
     *  目前只写了text类型，用于发送客资消息
     */
    private String type;
    private String userIdList;//用户id的字符串，多个用，分隔
    private String content;//消息内容

    public SendDingMsgDTO() {
    }

    public SendDingMsgDTO(String corpId, String type, String userIdList, String content) {
        this.corpId =corpId;
        this.type = type;
        this.userIdList = userIdList;
        this.content = content;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(String userIdList) {
        this.userIdList = userIdList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
