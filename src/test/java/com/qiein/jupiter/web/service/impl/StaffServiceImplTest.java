package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.vo.SearchStaffVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StaffServiceImplTest {

	@Autowired
	private StaffDao staffDao;

	@Test
	public void insert() {
	}

	@Test
	public void delete() {
	}

	@Test
	public void getById() {
	}

	@Test
	public void findList() {
	}

	@Test
	public void insert1() {
	}

	@Test
	public void setLockState() {
	}

	@Test
	public void setOnlineState() {
	}

	@Test
	public void delete1() {
	}

	@Test
	public void logicDelete() {
	}

	@Test
	public void update() {
	}

	@Test
	public void getById1() {
	}

	@Test
	public void findList1() {
	}

	@Test
	public void getCompanyList() {
	}

	@Test
	public void loginWithCompanyId() {
	}

	@Test
	public void heartBeatUpdate() {
	}

	@Test
	public void updateToken() {
	}

	@Test
	public void getGroupStaffs() {

	}

	@Test
	public void getStaffListBySearchKey() {
		List<SearchStaffVO> list = staffDao.getStaffListBySearchKey(1, "大");
		for (SearchStaffVO vo : list) {
			System.out.println(vo.getNickName());
		}
	}

}