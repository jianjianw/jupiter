package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    int editRole(RolePO rolePO);

    /**
     * 获取角色下拉框选项
     *
     * @param companyId
     * @return
     */
    List<RolePO> getRoleSelect(@Param("companyId") Integer companyId);
}
