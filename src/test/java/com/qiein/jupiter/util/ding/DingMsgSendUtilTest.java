package com.qiein.jupiter.util.ding;

import com.qiein.jupiter.web.entity.dto.SendDingMsgDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/7/13 16:37
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DingMsgSendUtilTest {
    @Autowired
    private DingMsgSendUtil dingMsgSendUtil;

    @Test
    public void sendDingMsg() {
        System.out.println(DingMsgSendUtil.dingUrl);
        for (int i =0 ;i<15;i++){
            dingMsgSendUtil.sendDingMsg("哇哇哇<br/><p>asdasd</p>"+i,2,59);
        }
    }
}