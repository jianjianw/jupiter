package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;

/**
 * 客资推送时封装客资信息
 *
 * @author JingChenglong 2018/05/08 10:39
 */
public class ClientPushDTO implements Serializable {

<<<<<<< HEAD
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
	 * 客服ID
	 */
	private int appointorId;
	/**
	 * 来源ID
	 */
	private int sourceId;
	/**
	 * 邀约名称
	 */
	private String appointName;

	/**
	 * 最终拍摄地名称
	 */
	private String filmingArea;

	/**
	 * 推送规则
	 */
	private int pushRule;

	/**
	 * 拍摄地ID
	 */
	private int shopId;

	/**
	 * 渠道ID
	 */
	private int channelId;

	/**
	 * 渠道类型
	 */
	private int channelTypeId;

	public ClientPushDTO(Integer pushRule, int companyId, String kzId, int shopId, int channelId, Integer channelTypeId,
			int overtime, int kzInterval) {
		this.pushRule = pushRule;
		this.companyId = companyId;
		this.kzId = kzId;
		this.shopId = shopId;
		this.channelId = channelId;
		this.channelTypeId = channelTypeId;
		this.overTime = overtime;
		this.pushInterval = kzInterval;
	}

	public ClientPushDTO() {
		super();
	}

	public boolean isNotEmpty() {
		return (NumUtil.isValid(this.pushRule) && NumUtil.isValid(this.companyId) && StringUtil.isValid(this.kzId)
				&& NumUtil.isValid(this.shopId) && NumUtil.isValid(this.channelId)
				&& NumUtil.isValid(this.channelTypeId));
	}

	public int getOverTime() {
		return overTime;
	}

	public void setOverTime(int overTime) {
		this.overTime = overTime;
	}

	public int getPushRule() {
		return pushRule;
	}

	public void setPushRule(int pushRule) {
		this.pushRule = pushRule;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPushInterval() {
		return pushInterval;
	}

	public void setPushInterval(int pushInterval) {
		this.pushInterval = pushInterval;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
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

	public String getAppointName() {
		return appointName;
	}

	public void setAppointName(String appointName) {
		this.appointName = appointName;
	}

	public String getFilmingArea() {
		return filmingArea;
	}
=======
    private static final long serialVersionUID = 1L;

    /**
     * 推送时间间隔
     */
    private int pushInterval;

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
     * 客服ID
     */
    private int appointorId;
    /**
     * 来源ID
     */
    private int sourceId;
    /**
     * 邀约名称
     */
    private String appointName;

    /**
     * 最终拍摄地名称
     */
    private String filmingArea;

    /**
     * 推送规则
     */
    private int pushRule;

    /**
     * 拍摄地ID
     */
    private int shopId;

    /**
     * 渠道ID
     */
    private int channelId;

    /**
     * 渠道类型
     */
    private int channelTypeId;
    /**
     * 客资姓名
     */
    private String kzName;
    /**
     * 客资手机号
     */
    private String kzPhone;
    /**
     * 客资微信
     */
    private String kzWechat;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 来源名称
     */
    private String sourceName;
    /**
     * 无效原因
     */
    private String invalidLabel;
    /**
     * 推广人ID
     */
    private int collectorId;
    /**
     * 订单时间
     */
    private int successTime;
    /**
     * 成交套系金额
     */
    private int amount;

    public int getPushRule() {
        return pushRule;
    }

    public void setPushRule(int pushRule) {
        this.pushRule = pushRule;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPushInterval() {
        return pushInterval;
    }

    public void setPushInterval(int pushInterval) {
        this.pushInterval = pushInterval;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public String getAppointName() {
        return appointName;
    }

    public void setAppointName(String appointName) {
        this.appointName = appointName;
    }

    public String getFilmingArea() {
        return filmingArea;
    }
>>>>>>> origin/dev

    public void setFilmingArea(String filmingArea) {
        this.filmingArea = filmingArea;
    }

    public String getKzName() {
        return kzName;
    }

    public void setKzName(String kzName) {
        this.kzName = kzName;
    }

    public String getKzPhone() {
        return kzPhone;
    }

    public void setKzPhone(String kzPhone) {
        this.kzPhone = kzPhone;
    }

    public String getKzWechat() {
        return kzWechat;
    }

    public void setKzWechat(String kzWechat) {
        this.kzWechat = kzWechat;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getInvalidLabel() {
        return invalidLabel;
    }

    public void setInvalidLabel(String invalidLabel) {
        this.invalidLabel = invalidLabel;
    }

    public int getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }

    public int getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(int successTime) {
        this.successTime = successTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}