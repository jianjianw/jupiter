package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
