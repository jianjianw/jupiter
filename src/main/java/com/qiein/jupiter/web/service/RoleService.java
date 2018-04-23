package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.entity.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色管理
 */
public interface RoleService {

    /**
     * 新增角色
     */
    int insert(String roleName, Integer companyId);

    /**
     * 删除角色
     *
     * @return
     */
    void delete(Integer roleId, Integer companyId);

    /**
     * 修改角色
     */
    void update(RoleVO roleVO);

    /**
     * 获取企业所有角色，以及角色对应的权限集合
     */
    List<RolePermissionVO> getCompanyAllRole(Integer companyId);

    /**
     * 获取角色下拉框选项
     *
     * @param companyId
     * @return
     */
    List<RolePO> getRoleSelect(Integer companyId);

}
