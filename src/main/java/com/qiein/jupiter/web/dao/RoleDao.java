package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RoleVO;

/**
 * 角色dao
 */
public interface RoleDao extends BaseDao<RolePO> {
	
    /*
     * 根据角色名获取角色信息
     */
    RolePO getRoleByName(@Param("roleName") String roleName, @Param("companyId") Integer companyId);

    /*
     * 修改角色
     */
    int editRole(RoleVO roleVO);

    /**
     * 获取角色下拉框选项
     *
     * @param companyId
     * @return
     */
    List<RolePO> getRoleSelect(@Param("companyId") Integer companyId);
}