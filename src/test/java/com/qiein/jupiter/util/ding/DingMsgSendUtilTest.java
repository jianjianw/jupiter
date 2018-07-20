package com.qiein.jupiter.util.ding;

import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.dao.StaffDao;
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
    @Autowired
    private StaffDao staffDao;

    @Test
    public void sendDingMsg() {
        StringBuffer sb = new StringBuffer();
        sb.append("恭喜您，您的客户在线订单啦<br/>");
        sb.append("编号：").append("2435325").append("<br/>");
        sb.append("姓名：").append("而我二").append("<br/>");
        sb.append("电话：").append("65324324").append("<br/>");
        sb.append("微信：").append("wecaht").append("<br/>");
        sb.append("渠道：").append("微博").append("<br/>");
        sb.append("来源：").append("微博1").append("<br/>");
        sb.append("客服：").append("叶").append("<br/>");
        sb.append("成交套系： ¥").append("59999").append("<br/>");
        sb.append("订单时间：").append("2022/22/22 22:22:22");
        DingMsgSendUtil.sendDingMsg(sb.toString(), 2, 59, staffDao);
    }
}