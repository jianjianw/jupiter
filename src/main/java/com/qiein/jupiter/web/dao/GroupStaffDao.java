package com.qiein.jupiter.web.dao;


import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组-员工 Dao
 */
public interface GroupStaffDao {

    /**
     * 根据公司id获取所有小组及下属员工
     *
     * @param companyId
     * @return
     */
//    List<GroupStaffVO> getAllGroupAndStaff(int companyId);

    /**
     * 根据parentId获取某个组下的所有员工
     *
     * @param parentId
     * @return
     */
    List<GroupStaffVO> getGroupStaffListByParentIdAndCid(
            @Param("companyId") int companyId,
            @Param("parentId") String parentId);

    /**
     * 删除员工小组关联
     *
     * @param companyId
     * @param staffId
     */
    public void deleteByStaffId(@Param("companyId") int companyId, @Param("staffId") int staffId);


    /*添加小组员工关联表*/
    public int insertGroupStaff(@Param("companyId") Integer companyId, @Param("groupId") String groupId, @Param("staffId") Integer staffId);

    /**
     * 批量删除员工小组关联
     *
     * @param companyId
     * @param staffIdArr
     */
    public void batchDeleteByStaffArr(@Param("companyId") Integer companyId, @Param("staffIdArr") String[] staffIdArr);

    /**
     * 批量编辑员工小组关联
     * @param companyId
     * @param staffIdArr
     * @param groupId
     */
    public void batchEditStaffGroup(@Param("companyId") Integer companyId, @Param("staffIdArr") String[] staffIdArr, @Param("groupId") String groupId);
}
