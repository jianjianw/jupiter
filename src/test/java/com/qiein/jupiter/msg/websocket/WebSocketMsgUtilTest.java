package com.qiein.jupiter.msg.websocket;

import com.qiein.jupiter.enums.OrderSuccessTypeEnum;
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
		OrderSuccessMsg orderSuccessMsg = new OrderSuccessMsg();
		orderSuccessMsg.setCompanyId(9999);
		orderSuccessMsg.setStaffName("张三1");
		orderSuccessMsg.setShopName("三亚");
		orderSuccessMsg.setAmount("12000");
		orderSuccessMsg.setType(OrderSuccessTypeEnum.TourShoot);
		orderSuccessMsg.setSrcImg(
				"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=156581925,3170808657&fm=27&gp=0.jpg");
		orderSuccessMsg.setHeadImg(
				"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4005596794,992112216&fm=27&gp=0.jpg");
		WebSocketMsgDTO webSocketMsgDTO = new WebSocketMsgDTO();
		webSocketMsgDTO.setCompanyId(1);
		webSocketMsgDTO.setType(WebSocketMsgEnum.OrderSuccess);
		long time = System.currentTimeMillis();
		webSocketMsgUtil.pushOrderSuccessMsg(orderSuccessMsg);
		System.out.println(System.currentTimeMillis() - time);
	}

}