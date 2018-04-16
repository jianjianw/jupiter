package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.StaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    public void batchInsertStaffRole(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param("roleIdArr") String[] roleIdArr);

    /**
     * 删除员工角色
     *
     * @param staffId
     * @param companyId
     */
    public void deleteByStaffId(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);

    /**
     * 批量删除员工角色关联
     *
     * @param companyId
     * @param staffIdArr
     */
    public void batchDeleteByStaffIdArr(@Param("companyId") Integer companyId, @Param("staffIdArr") String[] staffIdArr);

    /**
     * 批量添加多员工多角色
     * @param list
     */
    public void batchInsertStaffRoleByVO(@Param("list") List<StaffVO> list);
}
