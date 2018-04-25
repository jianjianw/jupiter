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
    GroupPO getByName(@Param("groupName") String groupName, @Param("companyId") int companyId);

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

    /**
     * 批量更新部门
     *
     * @param groupPOList
     * @return
     */
    int batchUpdateGroupType(List<GroupPO> groupPOList);

    /**
     * 获取员工所在组
     *
     * @param companyId
     * @param staffId
     * @param type
     * @return
     */
    List<String> getGroupByStaffAndType(@Param("companyId") int companyId, @Param("staffId") int staffId, @Param("type") String type);

    /**
     * 获取员工所在部门
     *
     * @param companyId
     * @param staffId
     * @param type
     * @return
     */
    List<String> getDeptByStaffAndType(@Param("companyId") int companyId, @Param("staffId") int staffId, @Param("type") String type);

    /**
     * 获取员工所在小组的信息
     *
     * @param staffId
     * @param companyId
     * @return
     */
    List<GroupPO> getGroupByStaffId(@Param("staffId") int staffId, @Param("companyId") int companyId);

    /**
     * 根据小组ID获取小组信息
     *
     * @param companyId
     * @param groupId
     * @return
     */
    GroupPO getGroupById(@Param("companyId") int companyId, @Param("groupId") String groupId);
}
