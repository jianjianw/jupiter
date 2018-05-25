package com.qiein.jupiter.web.service.quene;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.service.impl.ClientPushServiceImpl;

public class PushThread implements Runnable {

	private ClientPushServiceImpl service;

	private ClientPushDTO pushVO;

	@Override
	public void run() {
		// 推送客资消息
		try {
			if (isNotEmpty()) {
				this.service.pushLp(pushVO.getPushRule(), pushVO.getCompanyId(), pushVO.getKzId(), pushVO.getShopId(),
						pushVO.getChannelId(), pushVO.getChannelTypeId(), pushVO.getOverTime(),
						pushVO.getPushInterval());
			} else {
				System.out.println("客资信息为空，停止推送");
			}
		} catch (Exception e) {
			System.out.println("线程池中客资推送失败:" + e.getMessage());
		}
	}

	public boolean isNotEmpty() {
		return (this.service != null && this.pushVO.isNotEmpty());
	}

	public PushThread(ClientPushServiceImpl service, ClientPushDTO pushVO) {
		super();
		this.service = service;
		this.pushVO = pushVO;
	}

	public PushThread() {
		super();
	}

	public ClientPushServiceImpl getService() {
		return service;
	}

	public void setService(ClientPushServiceImpl service) {
		this.service = service;
	}

	public ClientPushDTO getPushVO() {
		return pushVO;
	}

	public void setPushVO(ClientPushDTO pushVO) {
		this.pushVO = pushVO;
	}
}