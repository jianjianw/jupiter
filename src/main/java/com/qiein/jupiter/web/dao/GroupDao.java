package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组/部门  Dao
 */
public interface GroupDao extends BaseDao<GroupPO> {

    /**
     * 根据部门名称获取
     *
     * @param groupName
     * @return
     */
    List<GroupPO> getByName(@Param("groupName") String groupName, @Param("companyId") int companyId);

    /**
     * 根据公司获取所有的组
     *
     * @param companyId
     * @return
     */
    List<GroupPO> findAllByCompanyId(int companyId);

    /**
     * 获取公司所有部门和小组
     *
     * @param companyId
     * @return
     */
    List<GroupVO> getCompanyAllDeptList(@Param("companyId") int companyId);

    /**
     * 根据父级id获取所有下级组
     *
     * @param parentId
     * @param companyId
     * @return
     */
    List<GroupPO> getByParentId(@Param("parentId") String parentId, @Param("companyId") int companyId);

    /**
     * 删除group
     *
     * @return
     */
    int deleteByGroupId(@Param("id") int id, @Param("companyId") int companyId);

}
