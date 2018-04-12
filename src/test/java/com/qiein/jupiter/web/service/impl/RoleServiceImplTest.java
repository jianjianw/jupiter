package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.RoleDao;
import com.qiein.jupiter.web.dao.RolePermissionDao;
import com.qiein.jupiter.web.entity.po.RolePO;
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
    private RolePermissionDao rolePmsDao;
    @Autowired
    private RoleDao roleDao;

    @Test
    public void insert() {
        String pmsIds = "1,2,3";
        RolePO exist = roleDao.getRoleByName("admin1", 1);
        if (exist!=null){
            throw  new RException(ExceptionEnum.ROLE_EXIST);
        }
        //1.添加角色表
        RolePO rolePO = new RolePO("admin1", 1, 1);
        roleDao.insert(rolePO);
        System.out.println(rolePO.getId());
        //2.添加角色权限关联表
        //3.添加角色权限关联表
        if (StringUtil.isNotNullStr(pmsIds)) {
            String[] pmsIdArr = pmsIds.split(CommonConstant.STR_SEPARATOR);
            rolePmsDao.batchAddRolePmsRela(rolePO.getId(), 1, pmsIdArr);
        }
    }

    @Test
    public void delete() {
        int i = roleDao.delete(16);
        if (i != 1) {
            throw new RException(ExceptionEnum.DELETE_FAIL);
        }
        rolePmsDao.deleteByRoleId(16, 1);
    }

    @Test
    public void update() {
        RolePO rolePO = new RolePO();
        rolePO.setId(1);
        rolePO.setCompanyId(1);
        rolePO.setRoleName("松松2");
        rolePO.setPriority(3);
        String pmsIds = "5,6,7";
        //1.修改角色表
        int i = roleDao.editRole(rolePO);
        if (i!=1){
            throw  new RException(ExceptionEnum.ROLE_EDIT_FAIL);
        }
        //2.删除角色权限关联表
        rolePmsDao.deleteByRoleId(rolePO.getId(),rolePO.getCompanyId());
        //3.插入角色权限关联表
        if (StringUtil.isNotNullStr(pmsIds)){
            String[] pmsIdArr = pmsIds.split(CommonConstant.STR_SEPARATOR);
            rolePmsDao.batchAddRolePmsRela(rolePO.getId(), rolePO.getCompanyId(), pmsIdArr);
        }
    }

    @Test
    public void getCompanyAllRole() {
        List<RolePermissionVO> companyAllRole = rolePmsDao.getCompanyAllRole(1);
        System.out.println(companyAllRole);
    }


}