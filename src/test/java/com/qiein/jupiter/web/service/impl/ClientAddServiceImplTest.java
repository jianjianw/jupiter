package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientAddServiceImplTest {

	@Autowired
	private ClientAddServiceImpl ClientAddServiceImpl;

	@Test
	public void addDs() {
		ClientVO clientVO = new ClientVO();
		clientVO.setChannelId(101);
		clientVO.setSourceId(107);
		clientVO.setShopId(65);
		clientVO.setKzName("JINGCHENGLONG");
		clientVO.setTypeId(1);
		clientVO.setKzPhone("27hjs8256");
		StaffPO staffPO = new StaffPO();
		staffPO.setCompanyId(2012);
		staffPO.setId(698);
		staffPO.setNickName("毒王");
		ClientAddServiceImpl.addDsClient(clientVO, staffPO);
	}
}