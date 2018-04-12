package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-权限 Dao
 */
public interface RolePermissionDao {

    /**
     * 获取企业所有角色，以及角色对应的权限集合
     */
    public List<RolePermissionVO> getCompanyAllRole(@Param("companyId") Integer companyId);

    /**
     * 批量添加角色权限关联表
     */
    public void batchAddRolePmsRela(@Param("roleId") Integer roleId, @Param("companyId") Integer companyId, @Param("pmsIdArr") String[] pmsIdArr);

    /**
     * 根据角色ID删除关联表
     */
    public void deleteByRoleId(@Param("roleId") Integer roleId, @Param("companyId") Integer companyId);

}
