package com.qiein.jupiter;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.service.quene.ThreadTaskPushManager;

public class ThreadTask {
	public static void main(String[] args) {
		ThreadTaskPushManager tpm = ThreadTaskPushManager.getInstance();
		for (int i = 1; i < 100; i++) {
			ClientPushDTO vo = new ClientPushDTO();
//			vo.setChannelId(1);
//			vo.setCompanyId(1);
//			vo.setPushRule(1);
//			vo.setKzId("121121");
//			vo.setTypeId(12);
//			vo.setChannelId(121);
//			vo.setChannelTypeId(12);
//			vo.setKzId(":::客资-" + i);
//			tpm.pushInfo(vo);
			try {
				// Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
