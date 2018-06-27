package com.qiein.jupiter.web.dao;


import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.po.GroupStaffPO;
import com.qiein.jupiter.web.entity.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

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
     * 获取小组人员详情
     *
     * @param companyId
     * @param groupId
     * @return
     */
    List<StaffMarsDTO> getGroupStaffsDetail(@Param("companyId") int companyId, @Param("groupId") String groupId);

    /**
     * 获取各小组内人员的接单数和在线人数
     *
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getStaffMarsInfo(@Param("companyId") int companyId);

    /**
     * 根据小组类型获取组内人员列表
     *
     * @param type
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getListByGroupType(@Param("type") String type, @Param("companyId") int companyId);

    /**
     * 获取公司所有的部门及下属员工
     *
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getAllDeptAndStaff(int companyId);

    /**
     * 获取员工所属角色
     *
     * @param companyId
     * @param staffId
     * @return
     */
    List<String> getStaffRoleList(@Param("companyId") int companyId, @Param("staffId") Integer staffId);

    /**
     * 根据不同类型，获取小组及人员
     *
     * @param companyId
     * @param type
     * @return
     */
    List<GroupBaseStaffVO> getGroupStaffByRole(@Param("companyId") int companyId, @Param("type") String type);

    /**
     * 根据角色，获取在线小组人员，用于分配
     *
     * @param companyId
     * @param type
     * @return
     */
    List<GroupBaseStaffVO> getOnLineStaffListByRole(@Param("companyId") int companyId, @Param("type") String type);

    /**
     * 批量添加小组人员
     *
     * @param companyId
     * @param groupId
     * @param staffIdArr
     */
    void batchInsertGroupStaff(@Param("companyId") int companyId, @Param("groupId") String groupId, @Param("staffIdArr") String[] staffIdArr);

    /**
     * 获取门市下面的所有人员列表
     *
     * @param companyId
     * @return
     */
    List<BaseStaffVO> getMsjdStaffList(@Param("companyId") int companyId);


    /**
     * 根据公司id和分组id获取分组员工的id集合
     * @param companyId
     * @param groupId
     * @return
     * */
    String getGroupStaffIdsStrByCompanyIdAndGroupId(@Param(value="companyId") int companyId,@Param(value="groupId") String groupId);

    /**
     * 根据员工id获取员工与组的关系
     * @param companyId
     * @param staffId
     * */
    List<GroupStaffPO> getGroupStaffByStaffId(@Param(value="companyId") int companyId,@Param(value="staffId") int staffId);

    /**
     * 删除指定某个组下员工的联系
     * @param companyId
     * @param staffId
     * @param groupId
     * */
    Integer deleteByStaffIdAndGroupId(@Param(value="staffId") int staffId,@Param(value="companyId") int companyId,@Param(value="groupId") String groupId);

}
