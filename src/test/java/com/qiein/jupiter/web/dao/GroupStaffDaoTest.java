package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupStaffDaoTest {

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Test
    public void getGroupAndStaffByParentId() {
        List<GroupStaffVO> groupAndStaffByParentId = groupStaffDao.getGroupStaffListByParentIdAndCid(1, "0");
    }

    @Test
    public void getAllGroupAndStaff() {
        List<GroupStaffVO> list = groupStaffDao.getAllGroupAndStaff(1);
    }
}