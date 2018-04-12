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
    public List<RolePermissionVO> getCompanyAllRole(@Param("companyId")Integer companyId);
}
