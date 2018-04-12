package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GroupPO;

import java.util.List;

/**
 * 组/部门  Dao
 */
public interface GroupDao extends BaseDao<GroupPO> {

    List<GroupPO> findAllByCompanyId(int companyId);
}
