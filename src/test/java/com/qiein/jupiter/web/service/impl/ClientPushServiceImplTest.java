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
		clientPushServiceImpl.pushLp(1, 9999, "f4eb0729b863e51749dfc2fd85026ddc", 52, 61, 1, 0, 0);
	}
}