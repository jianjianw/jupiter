package com.qiein.jupiter.web.service.quene;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushThread implements Runnable {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ClientPushDTO pushVO;

	@Override
	public void run() {
		// 推送客资消息
		try {
			if (isNotEmpty()) {
				this.pushVO.getService().pushLp(pushVO.getPushRule(), pushVO.getCompanyId(), pushVO.getKzId(),
						pushVO.getShopId(), pushVO.getChannelId(), pushVO.getChannelTypeId(), pushVO.getOverTime(),
						pushVO.getPushInterval());
			} else {
				log.info("客资信息为空，停止推送");
			}
		} catch (Exception e) {
			log.info("线程池中客资推送失败:" + e.getMessage());
		}
	}

	public boolean isNotEmpty() {
		System.out.println(this.pushVO.toString());
		return (this.pushVO != null && this.pushVO.isNotEmpty());
	}

	public PushThread(ClientPushDTO pushVO) {
		super();
		this.pushVO = pushVO;
	}

	public PushThread() {
		super();
	}

	public ClientPushDTO getPushVO() {
		return pushVO;
	}

	public void setPushVO(ClientPushDTO pushVO) {
		this.pushVO = pushVO;
	}
}