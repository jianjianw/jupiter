package com.qiein.jupiter.msg.dingding;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;

@Component
public class DingMsgUtil {

	// @Value("${webSocket.msgUrl}")
	private String msgUrl;

	public void postMsg(String content) {
		JSONObject msg = new JSONObject();
		JSONObject text = new JSONObject();
		msg.put("msgtype", "text");
		text.put("content", content);
		msg.put("text", text);
		HttpClient.textBody(msgUrl)
				// post提交json
				.json(msg.toJSONString()).execute();
	}
}
