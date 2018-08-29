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
     *
     * @param companyId
     * @return
     */
    List<RolePermissionVO> getCompanyAllRole(@Param("companyId") Integer companyId);

    /**
     * 批量添加角色权限关联表
     *
     * @param roleId
     * @param companyId
     * @param pmsIdArr
     */
    void batchAddRolePmsRela(@Param("roleId") Integer roleId, @Param("companyId") Integer companyId, @Param("pmsIdArr") String[] pmsIdArr);

    /**
     * 根据角色ID删除关联表
     *
     * @param roleId
     * @param companyId
     */
    void deleteByRoleId(@Param("roleId") Integer roleId, @Param("companyId") Integer companyId);

    /**
     * 获取员工权限集合
     *
     * @param companyId
     * @param staffId
     * @return
     */
    List<Integer> getStaffPmsList(@Param("companyId") Integer companyId, @Param("staffId") int staffId);

    /**
     * 判断该员工是否拥有授权客户端权限
     * @param companyId
     * @param staffId
     * @return
     */
    boolean checkStaffAuthPms(@Param("companyId")Integer companyId,@Param("staffId") Integer staffId);

}
