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

    @Test
    public void refuse() {
        service.refuse("fb02a517ef2b60d7c4dde5540381d414", "1", 2012, 698, "井成龙");
    }

    @Test
    public void pushClientNoticeInfo() {
        clientPushService.pushClientNoticeInfo();
    }
}