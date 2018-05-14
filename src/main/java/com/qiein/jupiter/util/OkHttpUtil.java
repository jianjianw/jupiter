package com.qiein.jupiter.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(20, TimeUnit.SECONDS).build();

	public static JSONObject doPost(String content, String url) throws IOException {
		JSONObject reply = null;
		RequestBody body = RequestBody.create(JSON, content);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = okHttpClient.newCall(request).execute();
		try {
			if (response.isSuccessful()) {
				String ss = response.body().string();
				reply = JSONObject.parseObject(ss);
			} else {
				// throw new IOException("Unexpected code " + response);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			if (response != null) {
				response.body().close();
				response.close();
			}
		}
		return reply;
	}

	public static JSONObject doGet(String url) throws IOException {
		JSONObject reply = null;
		Request request = new Request.Builder().url(url).build();

		Response response = okHttpClient.newCall(request).execute();
		try {
			if (response.isSuccessful()) {
				String ss = response.body().string();
				reply = JSONObject.parseObject(ss);
			} else {
				// throw new IOException("Unexpected code " + response);
			}
		} catch (Exception e) {

		} finally {
			if (response != null)
				try {
					response.body().close();
					response.close();
				} catch (Exception e) {
				}
		}
		return reply;
	}

	public static void main(String[] args) {

		JSONObject json = null;
		try {
			json = doGet("http://localhost:8080/hm-crm/test/openStoreMsg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(json);

	}

}