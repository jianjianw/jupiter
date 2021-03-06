package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.vo.GroupsInfoVO;

/**
 * 网销排班Dao
 * Created by Tt(叶华葳)
 * on 2018/5/10 0010.
 */
public interface SchedulingDao extends BaseDao<StaffMarsDTO> {
    /**
     * 根据类型获取部门
     * @param type
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getDeptListByType(@Param("type") String type,@Param("companyId") int companyId);

    /**
     * 根据部门编号获取部门下的所有小组
     * @param deptId
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getGroupListByDept(@Param("deptId") String deptId,@Param("companyId") int companyId);

    /**
     * 获取部门的今日接单数和当前在线人数
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getDeptLineNumAndOrderNum(@Param("companyId") int companyId);

    /**
     * 获取小组的今日接单数和当前在线人数
     * @param companyId
     * @return
     */
    List<GroupsInfoVO> getGroupLineNumAndOrderNum(@Param("companyId") int companyId,@Param("groupId") String groupId);

}
