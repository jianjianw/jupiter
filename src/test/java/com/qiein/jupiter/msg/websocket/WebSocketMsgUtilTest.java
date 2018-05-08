package com.qiein.jupiter.msg.websocket;

import com.qiein.jupiter.enums.WebSocketMsgEnum;
import com.qiein.jupiter.web.entity.dto.OrderSuccessMsg;
import com.qiein.jupiter.web.entity.dto.WebSocketMsgDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebSocketMsgUtilTest {

    @Autowired
    private WebSocketMsgUtil webSocketMsgUtil;

    @Test
    public void sendMsg() {
        OrderSuccessMsg orderSuccessMsg=new OrderSuccessMsg();
        orderSuccessMsg.setCompanyId(1);
        orderSuccessMsg.setStaffName("张三");
        orderSuccessMsg.setHeadImg("");
        orderSuccessMsg.setShopName("三亚");
        orderSuccessMsg.setAmount("12000");
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            webSocketMsgUtil.pushOrderSuccessMsg(orderSuccessMsg);
        }
        System.out.println(System.currentTimeMillis() - time);
    }


}