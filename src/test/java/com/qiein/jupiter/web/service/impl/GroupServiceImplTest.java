package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import com.qiein.jupiter.web.service.GroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupServiceImplTest {
    @Autowired
    private GroupService groupService;

    @Test
    public void getAllDeptAndStaff() {
        List<GroupStaffVO> allDeptAndStaff = groupService.getAllDeptAndStaff(1);
    }
}