package com.qiein.jupiter.web.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.service.impl.ClientPushServiceImpl;

/**
 * 定时客资分配
 * 
 * @author JingChenglong 2018/05/24 19:09
 *
 */
@Service
public class ClientPushTask {

	@Autowired
	private ClientPushServiceImpl pushService;

	@Scheduled(initialDelay = 1000, fixedDelay = 60 * 60 * 1000)
	public void taskPushLp() {
		int companyId = 2012;
		int interval = 120;
		int overTime = 180;
		System.out.println("开始执行客资定时推送任务");
		List<ClientPushDTO> infoList = pushService.getInfoListBeReadyPush(companyId, 120);
		pushInfo(companyId, overTime, interval, infoList);
		System.out.println("执行完毕");
	}

	/**
	 * 企业推送客资
	 * 
	 * @param companyId
	 * @param overTime
	 * @param interval
	 * @param infoList
	 */
	public void pushInfo(int companyId, int overTime, int interval, List<ClientPushDTO> infoList) {
		for (ClientPushDTO info : infoList) {
			pushService.pushLp(info.getPushRule(), companyId, info.getKzId(), info.getShopId(), info.getChannelId(),
					info.getChannelTypeId(), overTime, interval);
		}
	}
}