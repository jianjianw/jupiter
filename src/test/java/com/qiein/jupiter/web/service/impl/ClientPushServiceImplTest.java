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
		clientPushServiceImpl.pushLp(11, 9999, "3d449fefdae900a8c29fb249a358531e", 50, 61, 1, 0, 0);
	}
}