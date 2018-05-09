package com.qiein.jupiter.web.service;

/**
 * 客资领取
 * 
 * @author JingChenglong 2018/05/09 17:02
 *
 */
public interface ClientReceiveService {

	/**
	 * 客资领取
	 * 
	 * @param kzId
	 * @param logId
	 * @param companyId
	 * @param staffId
	 */
	public void receive(String kzId, String logId, int companyId, int staffId, String staffName);

	/**
	 * 客资拒接
	 * 
	 * @param kzId
	 * @param logId
	 * @param companyId
	 * @param staffId
	 */
	public void refuse(String kzId, String logId, int companyId, int staffId, String staffName);
}