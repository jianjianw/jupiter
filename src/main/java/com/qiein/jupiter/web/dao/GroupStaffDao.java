package com.qiein.jupiter.web.dao;


import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组-员工 Dao
 */
public interface GroupStaffDao {

    /**
     * 删除员工小组关联
     *
     * @param companyId
     * @param staffId
     */
    void deleteByStaffId(@Param("companyId") int companyId, @Param("staffId") int staffId);


    /**
     * 添加小组员工关联表
     *
     * @param companyId
     * @param groupId
     * @param staffId
     * @return
     */
    int insertGroupStaff(@Param("companyId") Integer companyId, @Param("groupId") String groupId,
                         @Param("staffId") Integer staffId);

    /**
     * 批量删除员工小组关联
     *
     * @param companyId
     * @param staffIdArr
     */
    void batchDeleteByStaffArr(@Param("companyId") Integer companyId, @Param("staffIdArr") String[] staffIdArr);

    /**
     * 批量编辑员工小组关联
     *
     * @param companyId
     * @param staffIdArr
     * @param groupId
     */
    void batchEditStaffGroup(@Param("companyId") Integer companyId, @Param("staffIdArr") String[] staffIdArr,
                             @Param("groupId") String groupId);

    /**
     * 获取小组人员
     *
     * @param companyId
     * @param groupId
     * @return
     */
    List<StaffVO> getGroupStaffs(@Param("companyId") int companyId, @Param("groupId") String groupId);

    /**
     * 根据小组类型获取组内人员列表
     * @param type
     * @param companyid
     * @return
     */
    List<GroupStaffVO> getListByGroupType(@Param("type") String type , @Param("companyId") int companyid);

    /**
     * 获取公司所有的部门及下属员工
     *
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getAllDeptAndStaff(int companyId);
}
