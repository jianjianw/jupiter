package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientReceiveServiceImplTest {

	@Autowired
	private ClientReceiveServiceImpl clientReceiveServiceImpl;

	@Test
	public void receive() {
		clientReceiveServiceImpl.receive("f0e3350ebf530eea0945a6f8cfb0b6bd", "1", 2012, 698, "小美女");

	}
}