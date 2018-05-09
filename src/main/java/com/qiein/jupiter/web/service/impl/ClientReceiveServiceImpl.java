package com.qiein.jupiter.web.service.impl;

import org.springframework.stereotype.Service;

import com.qiein.jupiter.web.service.ClientReceiveService;

/**
 * 客资领取
 * 
 * @author JingChenglong 2018/05/09 17:09
 *
 */
@Service
public class ClientReceiveServiceImpl implements ClientReceiveService {

	/**
	 * 客资领取
	 */
	@Override
	public void receive(String kzId, String logId, int companyId, int staffId) {

	}

	/**
	 * 客资拒接
	 */
	@Override
	public void refuse(String kzId, String logId, int companyId, int staffId) {

	}
}