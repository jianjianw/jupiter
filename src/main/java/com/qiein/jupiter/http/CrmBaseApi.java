package com.qiein.jupiter.http;

import java.util.Map;

import org.springframework.stereotype.Controller;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HmacSHA1Utils;
import com.qiein.jupiter.util.MD5Util;

/**
 * CRM接口调用
 * 
 * @author JingChenglong 2016-09-08 11:13
 *
 */
@Controller
public class CrmBaseApi extends BaseApi {

	private String url;// 接口调用地址
	private String accessid = "819974ff209236630597d53a593e1767";// 通行证编码
	private String key = "NzRhNDhkZGExNjFkZDBiNTRkMjI3Mjg4ODc1NDU2MzU=";// 签名

	public void setPurl(String purl) {
		this.url = "http://127.0.0.1:8888/hmcrm/http/HttpService";
	}

	// 签名类型(1 md5签名 ,2 hmacsh1 签名)
	private String signtype = "2";

	public String doService(Map<String, Object> reqcontent, String action) throws RException {
		initData(reqcontent);
		return doService(reqcontent, action, url);
	}

	public void doServiceUpLoad(Map<String, Object> reqcontent, String action, String filepath) throws RException {
		initData(reqcontent);
		doService(reqcontent, action, url, filepath, key);
	}

	private void initData(Map<String, Object> reqcontent) {
		reqcontent.put("requestType", "crm");
		reqcontent.put("accessid", accessid);
	}

	protected String getSign(String reqcontentStr) throws Exception {
		String sign = "";
		if (signtype.equals("2")) {
			sign = HmacSHA1Utils
					.signatureString(MD5Util.getMD5(reqcontentStr).toLowerCase(), key, CommonConstant.ENCODING_UTF8)
					.trim();
		} else {
			sign = MD5Util.getMD5(reqcontentStr).toLowerCase();
		}
		return sign;
	}
}