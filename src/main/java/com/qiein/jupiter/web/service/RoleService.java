package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.po.RolePO;
import com.qiein.jupiter.web.entity.vo.RolePermissionVO;
import com.qiein.jupiter.web.entity.vo.RoleVO;

/**
 * 角色管理
 */
public interface RoleService {

	/**
	 * 新增角色
	 */
	int insert(String roleName, Integer companyId);

	/**
	 * 删除角色
	 *
	 * @return
	 */
	void delete(Integer roleId, Integer companyId);

	/**
	 * 修改角色
	 */
	void update(RoleVO roleVO);

	/**
	 * 编辑角色排序
	 * 
	 * @param fId
	 * @param fPriority
	 * @param sId
	 * @param sPriority
	 * @param companyId
	 */
	void editProiority(Integer fId, Integer fPriority, Integer sId, Integer sPriority, Integer companyId);

	/**
	 * 获取企业所有角色，以及角色对应的权限集合
	 */
	List<RolePermissionVO> getCompanyAllRole(Integer companyId);

	/**
	 * 获取角色下拉框选项
	 *
	 * @param companyId
	 * @return
	 */
	List<RolePO> getRoleSelect(Integer companyId);

}
