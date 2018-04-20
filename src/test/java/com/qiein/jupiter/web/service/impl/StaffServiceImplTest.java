package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.vo.StaffPermissionVO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import com.qiein.jupiter.web.service.StaffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StaffServiceImplTest {

    @Autowired
    private StaffService staffService;
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
        List<StaffVO> list = staffDao.getStaffListBySearchKey(1, "大");
        for (StaffVO vo : list) {
            System.out.println(vo.getNickName());
        }
    }

    @Test
    @Transactional
    public void getStaffPermissionById() {
        StaffPermissionVO staffPermissionById = staffService.getStaffPermissionById(1, 1);
        System.out.println(staffPermissionById);
    }
}