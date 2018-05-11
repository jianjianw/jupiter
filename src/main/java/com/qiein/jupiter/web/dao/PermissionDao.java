package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.po.PermissionPO;

/**
 * 权限Dao
 */
public interface PermissionDao extends BaseDao<PermissionPO> {

    /**
     * 获取员工信息及角色权限信息
     *
     * @return
     */
    List<PermissionPO> getStaffPermission(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);
}