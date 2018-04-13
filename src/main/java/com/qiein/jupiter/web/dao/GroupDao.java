package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组/部门  Dao
 */
public interface GroupDao extends BaseDao<GroupPO> {

    List<GroupPO> findAllByCompanyId(int companyId);

    /*获取公司所有部门和小组*/
    public List<GroupVO> getCompanyAllDeptList(@Param("companyId") Integer companyId);

    /*添加小组员工关联表*/
    public int insertGroupStaff(@Param("companyId") Integer companyId, @Param("groupId") String groupId, @Param("staffId") Integer staffId);
}
