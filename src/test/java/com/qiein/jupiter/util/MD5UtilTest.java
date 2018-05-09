package com.qiein.jupiter.util;

import org.junit.Assert;
import org.junit.Test;

public class MD5UtilTest {

	@Test
	public void getMD5() {
		Assert.assertTrue(MD5Util.getMD5("123456").equals("e10adc3949ba59abbe56e057f20f883e"));
	}

	@Test
	public void getMD5ByLoop() {
	}

	@Test
	public void getSaltMd5() {
		System.out.println(MD5Util.getSaltMd5("123456"));
	}
}