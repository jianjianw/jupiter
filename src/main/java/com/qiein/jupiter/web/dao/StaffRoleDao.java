package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.vo.StaffVO;

/**
 * 员工-角色 Dao
 */
public interface StaffRoleDao {

    /**
     * 批量添加员工角色
     *
     * @param staffId
     * @param companyId
     * @param roleIdArr
     */
    void batchInsertStaffRole(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param("roleIdArr") String[] roleIdArr);

    /**
     * 删除员工角色
     *
     * @param staffId
     * @param companyId
     */
    void deleteByStaffId(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);

    /**
     * 批量删除员工角色关联
     *
     * @param companyId
     * @param staffIdArr
     */
    void batchDeleteByStaffIdArr(@Param("companyId") Integer companyId, @Param("staffIdArr") String[] staffIdArr);

    /**
     * 批量添加多员工多角色
     *
     * @param list
     */
    void batchInsertStaffRoleByVO(@Param("list") List<StaffVO> list);

    /**
     * 根据权限查询员工人数
     *
     * @param companyId
     * @param roleId
     * @return
     */
    int getCountByRole(@Param("companyId") int companyId, @Param("roleId") int roleId);

}
