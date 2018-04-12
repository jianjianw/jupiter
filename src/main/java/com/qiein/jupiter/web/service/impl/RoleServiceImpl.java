package com.qiein.jupiter.web.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.RoleDao;
import com.qiein.jupiter.web.dao.RolePermissionDao;
import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolePermissionDao rolePmsDao;//角色权限关联持久化
    @Autowired
    private RoleDao roleDao;//角色持久化

    /**
     * 新增角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(String roleName, Integer priority, String pmsIds, Integer companyId) {
        //1.查是否已存在
        RolePO exist = roleDao.getRoleByName(roleName, companyId);
        if (exist != null) {
            throw new RException(ExceptionEnum.ROLE_EXIST);
        }
        //2.添加角色表
        RolePO rolePO = new RolePO(roleName, companyId, priority);
        roleDao.insert(rolePO);
        //3.添加角色权限关联表
        if (StringUtil.isNotNullStr(pmsIds)) {
            String[] pmsIdArr = pmsIds.split(CommonConstant.STR_SEPARATOR);
            rolePmsDao.batchAddRolePmsRela(rolePO.getId(), companyId, pmsIdArr);
        }
    }

    /**
     * 删除角色
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer roleId, Integer companyId) {
        int i = roleDao.delete(roleId);
        if (i != 1) {
            throw new RException(ExceptionEnum.DELETE_FAIL);
        }
        rolePmsDao.deleteByRoleId(roleId, companyId);
    }

    /**
     * 修改角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RolePO rolePO, String pmsIds) {
        //1.查重
        RolePO exist = roleDao.getRoleByName(rolePO.getRoleName(), rolePO.getCompanyId());
        if (exist != null && exist.getId() != rolePO.getId()){
            throw new RException(ExceptionEnum.ROLE_EXIST);
        }
        //2.修改角色表
        int i = roleDao.editRole(rolePO);
        if (i != 1) {
            throw new RException(ExceptionEnum.ROLE_EDIT_FAIL);
        }
        //3.删除角色权限关联表
        rolePmsDao.deleteByRoleId(rolePO.getId(), rolePO.getCompanyId());
        //4.插入角色权限关联表
        if (StringUtil.isNotNullStr(pmsIds)) {
            String[] pmsIdArr = pmsIds.split(CommonConstant.STR_SEPARATOR);
            rolePmsDao.batchAddRolePmsRela(rolePO.getId(), rolePO.getCompanyId(), pmsIdArr);
        }
    }

    /**
     * 获取企业所有角色，以及角色对应的权限集合
     */
    public List<RolePermissionVO> getCompanyAllRole(Integer companyId) {
        return rolePmsDao.getCompanyAllRole(companyId);
    }
}
