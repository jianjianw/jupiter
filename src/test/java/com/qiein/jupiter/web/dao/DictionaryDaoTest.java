package com.qiein.jupiter.web.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.web.entity.vo.MenuVO;

public class DictionaryDaoTest {
	@Autowired
	private DictionaryDao dictionaryDao;

	@Test
	public void getCompanyMemu() {
		List<MenuVO> list = dictionaryDao.getCompanyMenu(0, DictionaryConstant.MENU_TYPE);
		System.out.println(list.size());
	}
}