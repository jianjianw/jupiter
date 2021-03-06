package com.qiein.jupiter.http;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.HmacSHA1Utils;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.TimeUtil;

/**
 * 接口调用基类
 * 
 * @author JingChenglong 2018/04/23 19"04
 *
 */
public abstract class BaseApi {

	protected String format = "json";

	public String doService(Map<String, Object> reqcontent, String action, String url) throws RException {
		Map<String, Object> requestMap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, Object> common = new LinkedHashMap<String, Object>();
		common.put("action", action);
		common.put("reqtime", TimeUtil.getSysTimeLong());
		map.put("common", common);
		map.put("content", reqcontent);
		requestMap.put("request", map);
		String reqcontentStr = JSON.toJSONString(requestMap);
		String response = "";
		try {
			response = HttpServiceUtil.doService(url, format, getSign(reqcontentStr), reqcontentStr);
		} catch (Exception e) {
			throw new RException(ExceptionEnum.RPC_ERROR);
		}
		return response;
	}

	protected abstract String getSign(String reqcontentStr) throws Exception;

	public String doService(Map<String, Object> reqcontent, String action, String url, String filepath, String key)
			throws RException {
		reqcontent.put("action", action);
		reqcontent.put("reqtime", TimeUtil.getSysTimeLong());
		reqcontent.put("reqlength", getFileLength(filepath));
		reqcontent.put("reqcontentmd5", MD5Util.md5file(filepath).toLowerCase());
		try {
			String sign = HmacSHA1Utils.signatureString(String.valueOf(reqcontent.get("reqcontentmd5")), key, "UTF-8")
					.trim();
			reqcontent.put("sign", sign);
			HttpServiceUtil.doUpService(url, format, reqcontent, filepath);
		} catch (Exception e) {
		}
		return "";
	}

	public static long getFileLength(String filename) {

		File file = new File(filename);
		if (file.isFile() && file.exists()) {
			return file.length();
		}

		return 0L;
	}
}