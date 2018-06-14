package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * FileName: ClientStatusVoteVO
 *
 * @author: yyx
 * @Date: 2018-6-14 16:09
 */
public class ClientStatusVoteVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 状态类型
     * 1. 有效 2. 待定 3. 无效
     * */
    private Integer type;

    /**
     * 客资id
     * */
    private String kzId;
    /**
     * 备注
     * */
    private String content;

    /**
     * 状态id
     * */
    private Integer statusId;

    /**
     * 收集人id
     * */
    private Integer collectorId;

    /**
     * 渠道名称
     * */
    private String channelName;
    /**
     * 客资名称
     * */
    private String kzName;
    /**
     * 客资微信
     * */
    private String kzWeChat;
    /**
     * 客资QQ
     * */
    private String kzQq;
    /**
     * 客资手机号
     * */
    private String kzPhone;

    /**
     * 公司id
     * */
    private Integer companyId;

    /**
     * 操作者id
     * */
    private Integer operaId;

    /**
     * 操作者名称
     * */
    private String operaName;


    public Integer getOperaId() {
        return operaId;
    }

    public void setOperaId(Integer operaId) {
        this.operaId = operaId;
    }

    public String getOperaName() {
        return operaName;
    }

    public void setOperaName(String operaName) {
        this.operaName = operaName;
    }

    public void setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
    }

    public Integer getCollectorId() {
        return collectorId;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getKzWeChat() {
        return kzWeChat;
    }

    public void setKzWeChat(String kzWeChat) {
        this.kzWeChat = kzWeChat;
    }

    public String getKzQq() {
        return kzQq;
    }

    public void setKzQq(String kzQq) {
        this.kzQq = kzQq;
    }

    public String getKzPhone() {
        return kzPhone;
    }

    public void setKzPhone(String kzPhone) {
        this.kzPhone = kzPhone;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
