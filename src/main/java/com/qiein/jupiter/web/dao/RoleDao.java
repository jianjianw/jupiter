package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.RolePO;
import org.apache.ibatis.annotations.Param;

/**
 * 角色dao
 */
public interface RoleDao extends BaseDao<RolePO> {
    /*
     * 根据角色名获取角色信息
     */
    public RolePO getRoleByName(@Param("roleName") String roleName, @Param("companyId") Integer companyId);

    /*
     * 修改角色
     */
    public int editRole(RolePO rolePO);
}
