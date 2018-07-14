package com.qiein.jupiter.util.ding;

import com.qiein.jupiter.web.entity.dto.SendDingMsgDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void sendDingMsg() {
        System.out.println(DingMsgSendUtil.dingUrl);
        for (int i =0 ;i<15;i++){
            DingMsgSendUtil.sendDingMsg(new SendDingMsgDTO("dinga3bceec769e8167d35c2f4657eb6378f",DingMsgSendUtil.MSG_TYPE_TEXT,"15879,manager258","吴亚武是傻瓜"));
        }
    }
}