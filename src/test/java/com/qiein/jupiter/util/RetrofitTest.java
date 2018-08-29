package com.qiein.jupiter.util;

import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.web.service.ApolloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: shiTao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RetrofitTest {
    @Autowired
    private ApolloService apolloService;

    @Autowired
    private WebSocketMsgUtil webSocketMsgUtil;


    @Test
    public void test1() {
        apolloService.getCrmUrlByCidFromApollo(3901);
    }

    @Test
    public void test12() throws InterruptedException {
        webSocketMsgUtil.pushAlertMsg(3861, 340, "hello world");
    }
}

