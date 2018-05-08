package com.qiein.jupiter.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrmBaseApiTest {

	@Autowired
	private CrmBaseApi crmBaseApi;

	@Test
	public void testGet() {
		Map<String, Object> reqContent = new HashMap<String, Object>();
		reqContent.put("companyid", 1);
		String str = crmBaseApi.doService(reqContent, "addClientInfoPcDsLp");
		System.out.println(str);
	}
}