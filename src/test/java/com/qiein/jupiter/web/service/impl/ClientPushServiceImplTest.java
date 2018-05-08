package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientPushServiceImplTest {

	@Autowired
	private ClientPushServiceImpl clientPushServiceImpl;

	@Test
	public void getAllDeptAndStaff() {
		clientPushServiceImpl.pushLp(1, 8888, "e76cba71dbbbb1ee0016770edeeeb7a3", 1, 1, 1, 0, 0);
	}
}