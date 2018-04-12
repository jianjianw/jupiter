package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色管理
 */
public interface RoleService {

    /**
     * 新增角色
     */
    void insert(String roleName, Integer priority, String pmsIds, Integer companyId);

    /**
     * 删除角色
     *
     * @return
     */
    void delete(Integer roleId, Integer companyId);

    /**
     * 修改角色
     */
    void update(RolePO rolePO, String pmsIds);

    /**
     * 获取企业所有角色，以及角色对应的权限集合
     */
    public List<RolePermissionVO> getCompanyAllRole(Integer companyId);


}
