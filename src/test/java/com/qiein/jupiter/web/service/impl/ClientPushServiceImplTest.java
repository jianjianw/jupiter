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
		clientPushServiceImpl.pushLp(1, 2012, "ac2ee1238e8cf2a62c4052180aa3a0c9", 70, 101, 1, 120, 180);
	}
}