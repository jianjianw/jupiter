package com.qiein.jupiter.util.wechat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.SendingContext.RunTime;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @create by Tt(叶华葳) 2018-06-12 14:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatPushUtilTest {
    @Test
    public void getQRCode() throws Exception {
        String cidAndUid = "cid_"+1+"&uid_"+12+"&url_"+WeChatPushUtil.APOLLO_URL.replace("http://","");
        System.out.println(cidAndUid);
    }

}