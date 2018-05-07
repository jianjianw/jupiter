package com.qiein.jupiter.msg.websocket;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.dto.CompanyMsgDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebSocketMsgUtilTest {

    @Autowired
    private WebSocketMsgUtil webSocketMsgUtil;

    @Test
    public void sendMsg() {
        CompanyMsgDTO companyMsgDTO = new CompanyMsgDTO();
        companyMsgDTO.setCompanyId(1);
        companyMsgDTO.setContent("hhhhh");
        companyMsgDTO.setType("1");
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            webSocketMsgUtil.sendMsg(companyMsgDTO);
        }
        System.out.println(System.currentTimeMillis() - time);
    }


}