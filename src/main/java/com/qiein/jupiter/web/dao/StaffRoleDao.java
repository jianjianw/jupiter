package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 员工-角色 Dao
 */
public interface StaffRoleDao {

    /*批量添加员工角色*/
    public void batchInsertStaffRole(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param("roleIdArr") String[] roleIdArr);

    /**
     * 删除员工角色
     *
     * @param staffId
     * @param companyId
     */
    public void deleteByStaffId(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);
}
