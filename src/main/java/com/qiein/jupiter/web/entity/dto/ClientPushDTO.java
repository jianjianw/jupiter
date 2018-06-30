package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.service.impl.ClientPushServiceImpl;

/**
 * 客资推送时封装客资信息
 *
 * @author JingChenglong 2018/05/08 10:39
 */
public class ClientPushDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 推送时间间隔
     */
    private int pushInterval;

    /**
     * 客资领取超时时间
     */
    private int overTime;

    /**
     * 企业ID
     */
    private int companyId;

    /**
     * 主键ID
     */
    private int id;

    /**
     * 客资ID
     */
    private String kzId;

    /**
     * 状态ID
     */
    private int statusId;

    /**
     * 推广ID
     */
    private int collectorId;

    /**
     * 客服ID
     */
    private int appointorId;
    /**
     * 来源ID
     */
    private int sourceId;

    /**
     * 推送规则
     */
    private int pushRule;
    /**
     * 门店ID
     */
    private int shopId;

    /**
     * 拍摄类型ID
     */
    private int typeId;

    /**
     * 渠道ID
     */
    private int channelId;

    /**
     * 渠道类型
     */
    private int channelTypeId;

    private ClientPushServiceImpl service;

    public ClientPushDTO(ClientPushServiceImpl service, Integer pushRule, int companyId, String kzId, int typeId,
                         int channelId, Integer channelTypeId, int overtime, int kzInterval, int srcId) {
        this.pushRule = pushRule;
        this.companyId = companyId;
        this.kzId = kzId;
        this.typeId = typeId;
        this.channelId = channelId;
        this.channelTypeId = channelTypeId;
        this.overTime = overtime;
        this.pushInterval = kzInterval;
        this.service = service;
        this.sourceId = srcId;
    }

    public ClientPushDTO() {
        super();
    }

    public boolean isNotEmpty() {
        return (service != null && NumUtil.isValid(this.pushRule) && NumUtil.isValid(this.companyId)
                && StringUtil.isValid(this.kzId) && NumUtil.isValid(this.channelId)
                && NumUtil.isValid(this.channelTypeId));
    }

    public int getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }

    public ClientPushServiceImpl getService() {
        return service;
    }

    public void setService(ClientPushServiceImpl service) {
        this.service = service;
    }

    public int getPushInterval() {
        return pushInterval;
    }

    public void setPushInterval(int pushInterval) {
        this.pushInterval = pushInterval;
    }

    public int getOverTime() {
        return overTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKzId() {
        return kzId;
    }

    public void setKzId(String kzId) {
        this.kzId = kzId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAppointorId() {
        return appointorId;
    }

    public void setAppointorId(int appointorId) {
        this.appointorId = appointorId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getPushRule() {
        return pushRule;
    }

    public void setPushRule(int pushRule) {
        this.pushRule = pushRule;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(int channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "ClientPushDTO [pushInterval=" + pushInterval + ", overTime=" + overTime + ", companyId=" + companyId
                + ", id=" + id + ", kzId=" + kzId + ", statusId=" + statusId + ", appointorId=" + appointorId
                + ", sourceId=" + sourceId + ", pushRule=" + pushRule + ", typeId=" + typeId + ", channelId="
                + channelId + ", channelTypeId=" + channelTypeId + ", service=" + service + "]";
    }


}