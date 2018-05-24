package com.qiein.jupiter.web.service.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
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
	@Autowired
	private CompanyDao companyDao;

	/**
	 * 定时任务-推送客资
	 */
	@Scheduled(initialDelay = 1000, fixedDelay = 30 * 1000)
	public void taskPushLp() {
		List<CompanyPO> compList = companyDao.listComp();
		for (CompanyPO comp : compList) {
			List<ClientPushDTO> infoList = pushService.getInfoListBeReadyPush(comp.getId(), comp.getOvertime());
			pushInfo(comp.getId(), comp.getOvertime(), comp.getKzInterval(), infoList);
		}
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
		if (CollectionUtils.isEmpty(infoList)) {
			return;
		}
		for (ClientPushDTO info : infoList) {
			pushService.pushLp(info.getPushRule(), companyId, info.getKzId(), info.getShopId(), info.getChannelId(),
					info.getChannelTypeId(), overTime, interval);
		}
	}
}