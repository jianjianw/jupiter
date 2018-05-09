package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.CompanyService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyServiceImplTest {

	@Autowired
	private CompanyService companyService;

	@Test
	public void getById() {
		CompanyPO companyPO = companyService.getById(1);
		System.out.println(companyPO);
	}

	@Test
	public void findList() {
	}

	@Test
	public void deleteFlag() {
	}

	@Test
	public void insert() {
	}

	@Test
	public void update() {
	}
}