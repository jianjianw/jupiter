package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.entity.vo.GroupBaseStaffVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.service.GroupService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupServiceImplTest {
    @Autowired
    private GroupService groupService;

    @Test
    public void getAllDeptAndStaff() {
        groupService.getAllDeptAndStaff(1);
    }

    @Test
    public void test1() {
        List<GroupBaseStaffVO> allGroupStaff = groupService.getAllGroupStaff(1);
        System.out.println(allGroupStaff);
    }
}