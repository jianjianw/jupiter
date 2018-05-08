package com.qiein.jupiter.web.service;

/**
 * 客资推送
 * 
 * @author JingChenglong 2018/05/08 10:27
 *
 */
public interface ClientPushService {

	/**
	 * 旅拍版本客资推送
	 * 
	 * @param rule-推送规则
	 * @param companyId-企业ID
	 * @param kzId-客资ID
	 * @param shopId-拍摄地ID
	 * @param channelId-渠道ID
	 * @param overTime-领单超时时间
	 * @param interval-领单间隔时间
	 */
	public void pushLp(int rule, int companyId, String kzId, int shopId, int channelId, int overTime, int interval);
}