package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.RolePermissionDao;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceImplTest {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Test
    public void insert() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getCompanyAllRole() {
        List<RolePermissionVO> companyAllRole = rolePermissionDao.getCompanyAllRole(1);
        System.out.println(companyAllRole);
    }
}