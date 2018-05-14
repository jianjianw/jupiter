package com.qiein.jupiter.util;

import java.io.IOException;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;

public class WebUtil {

	public static String getIpLocation(String ip) {
		JSONObject json = null;
		String area = "未知";
		try {
			json = OkHttpUtil
					.doGet("http://apis.juhe.cn/ip/ip2addr?ip=" + ip + "&key=942fb48c90bddcb85b61d6908c566dc1");
			if (json != null) {
				if (json.getString("resultcode") != null) {
					if (json.getString("resultcode").equals("200")) {
						area = json.getJSONObject("result").getString("area");
					}
				}
			}
		} catch (IOException e) {
			return area;
		}

		area = area.replaceAll("省", CommonConstant.STR_SEPARATOR);
		area = area.replaceAll("市", CommonConstant.STR_SEPARATOR);
		if (area.endsWith(CommonConstant.STR_SEPARATOR)) {
			area += CommonConstant.STR_SEPARATOR;
		}
		return area;

	}
}
