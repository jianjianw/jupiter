package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.RolePermissionVO;

import java.util.List;

/**
 * 角色管理
 */
public interface RoleService {

    /**
     * 新增角色
     *
     * @return
     */
    int insert();

    /**
     * 删除角色
     *
     * @return
     */
    int delete();

    /**
     * 修改角色
     */
    int update();

    /**
     * 获取企业所有角色，以及角色对应的权限集合
     */
    public List<RolePermissionVO> getCompanyAllRole(Integer companyId);


}
