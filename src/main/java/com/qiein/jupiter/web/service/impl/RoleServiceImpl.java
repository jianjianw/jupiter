package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.PmsConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.RoleDao;
import com.qiein.jupiter.web.dao.RolePermissionDao;
import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.entity.vo.RoleVO;
import com.qiein.jupiter.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolePermissionDao rolePmsDao;

    @Autowired
    private RoleDao roleDao;

    /**
     * 新增角色
     */
    @Transactional(rollbackFor = Exception.class)
    public int insert(String roleName, Integer companyId) {
        //1.查是否已存在
        RolePO exist = roleDao.getRoleByName(roleName, companyId);
        if (exist != null) {
            throw new RException(ExceptionEnum.ROLE_EXIST);
        }
        //2.添加角色表
        RolePO rolePO = new RolePO(roleName, companyId);
        roleDao.insert(rolePO);
        //3.添加默认权限，查看个人，编辑个人
        rolePmsDao.batchAddRolePmsRela(rolePO.getId(), companyId, PmsConstant.DEFAULT_PMS_ARR);
        return rolePO.getId();
    }

    /**
     * 删除角色
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer roleId, Integer companyId) {
        //TODO 检查该角色是否绑定人员，如果有提示不能删除
        //throw new RException(ExceptionEnum.ROLE_DELETE_ERROR);
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
    public void update(RoleVO roleVO) {
        //1.查重
        RolePO exist = roleDao.getRoleByName(roleVO.getRoleName(), roleVO.getCompanyId());
        if (exist != null && exist.getId() != roleVO.getRoleId()) {
            throw new RException(ExceptionEnum.ROLE_EXIST);
        }
        //2.修改角色表
        int i = roleDao.editRole(roleVO);
        if (i != 1) {
            throw new RException(ExceptionEnum.ROLE_EDIT_FAIL);
        }
        //3.删除角色权限关联表
        rolePmsDao.deleteByRoleId(roleVO.getRoleId(), roleVO.getCompanyId());
        //4.插入角色权限关联表
        if (StringUtil.isNotEmpty(roleVO.getPmsIds())) {
            String[] pmsIdArr = roleVO.getPmsIds().split(CommonConstant.STR_SEPARATOR);
            rolePmsDao.batchAddRolePmsRela(roleVO.getRoleId(), roleVO.getCompanyId(), pmsIdArr);
        }
    }

    /**
     * 编辑角色排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @param companyId
     */
    @Override
    @Transactional
    public void editProiority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId) {
        roleDao.updatePriority(fId, fPriority, companyId);
        roleDao.updatePriority(sId, sPriority, companyId);
    }

    /**
     * 获取企业所有角色，以及角色对应的权限集合
     */
    public List<RolePermissionVO> getCompanyAllRole(Integer companyId) {
        return rolePmsDao.getCompanyAllRole(companyId);
    }

    /**
     * 获取角色下拉框选项
     *
     * @param companyId
     * @return
     */
    public List<RolePO> getRoleSelect(Integer companyId) {
        return roleDao.getRoleSelect(companyId);
    }
}
