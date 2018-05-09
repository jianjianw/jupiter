package com.qiein.jupiter.util;

import org.junit.Test;

import com.qiein.jupiter.web.entity.po.StaffPO;

public class ObjectUtilTest {

	@Test
	public void reflect() {

	}

	@Test
	public void objectStrParamTrim() throws Exception {
		StaffPO staffPO = new StaffPO();
		staffPO.setUserName("    123              ");
		staffPO.setPhone("1231231231231231");
		ObjectUtil.objectStrParamTrim(staffPO);
	}
}