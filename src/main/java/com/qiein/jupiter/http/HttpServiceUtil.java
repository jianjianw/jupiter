package com.qiein.jupiter.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpException;

import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.StringUtil;

public class HttpServiceUtil {

	private static final String CHARSET_ENCODING = "UTF-8";
	private static final String HTTPMETHOD_POST = "POST";

	public static String doService(String url, String format, String sign, String reqcontent) throws RException {

		HttpClient httpClient = null;
		String rstStr = "";
		try {
			httpClient = getClient(url, format);

			reqcontent = StringUtil.nullToStrTrim(reqcontent);

			if (StringUtil.isNotEmpty(reqcontent)) {
				httpClient.addHeader("reqlength", String.valueOf(StringUtil.getRealLength(reqcontent)));

				if (StringUtil.isNotEmpty(sign)) {
					httpClient.addHeader("sign", StringUtil.encode(sign));
				}
				httpClient.setEntity(reqcontent, CHARSET_ENCODING);
			}
			httpClient.execute();

			boolean isfile = false;
			String fileName = "";
			Header[] responseHeaders = httpClient.getAllHeaders();
			for (Header header : responseHeaders) {
				if ((StringUtil.decode(header.getValue())).indexOf("attachment;") != -1) {
					String tp = StringUtil.decode(header.getValue());
					fileName = tp.substring(tp.indexOf("filename") + 9, tp.length());
					fileName = fileName.replaceAll("\"", "");
					isfile = true;
				}
			}
			if (isfile) {
				httpClient.getContentFile("E:\\down_" + fileName);
			} else {
				rstStr = httpClient.getContentStr();
			}
		} catch (Exception e) {
			httpClient.abortExecution();
			throw new RException(e.getMessage());
		} finally {
			httpClient.shutdown();
		}
		return rstStr;
	}

	public static Map<String, Object> doUpService(String url, String format, Map<String, Object> reqcontent,
			String filepath) throws RException {
		HttpClient httpClient = null;
		Map<String, Object> result = null;

		try {
			httpClient = getClient(url, format);

			if (reqcontent != null && !reqcontent.isEmpty()) {
				httpClient.addHeaderObj(reqcontent);
			}

			httpClient.setEntity(filepath);
			httpClient.execute();
		} catch (Exception e) {
			httpClient.abortExecution();
			throw new RException(e.getMessage());
		} finally {
			httpClient.shutdown();
		}
		return result;
	}

	public static HttpClient getClient(String url, String format) throws MalformedURLException, HttpException {
		HttpClient httpClient = new HttpClientImpl();

		String httpVersion = "1.1";
		String httpMethod = HTTPMETHOD_POST;
		int requestTimeoutInMillis = 60000;
		httpClient.setUrl(url);

		httpClient.setHttpVersion(httpVersion);
		httpClient.setHttpContentCharset(CHARSET_ENCODING);
		httpClient.setRequestTimeoutInMillis(requestTimeoutInMillis);

		httpClient.setHttpMethod(httpMethod);

		if (StringUtil.isNotEmpty(format)) {
			httpClient.addHeader("format", StringUtil.encode(format));
		}
		return httpClient;
	}

	public static String encode(String str, String enc) {

		String strEncode = "";

		try {
			if (str != null)
				strEncode = URLEncoder.encode(str, enc);
		} catch (UnsupportedEncodingException e) {
		}

		return strEncode;
	}
}