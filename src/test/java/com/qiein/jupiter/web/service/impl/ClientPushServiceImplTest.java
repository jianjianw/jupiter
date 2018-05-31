package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.service.ClientPushService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientPushServiceImplTest {

	@Autowired
	private ClientReceiveServiceImpl service;

	@Autowired
	private ClientPushService clientPushService;

//	@Test
	public void refuse() {
		service.refuse("fb02a517ef2b60d7c4dde5540381d414", "1", 2012, 698, "井成龙");
	}

	@Test
	public void pushClientNoticeInfo() {
		clientPushService.pushLp(
				"2a548e0f7c1789fe9075741a0fb130a2,5255f8475f0a59995c3eb66fdff4de2,6de81cb93988381a4f50dc110d646b,10e740ac3f0c72f785187b80271a6c",
				"15,23", 1, 2, "特斯拉");
	}
}