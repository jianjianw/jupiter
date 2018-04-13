package com.qiein.jupiter.web.dao;


import com.qiein.jupiter.web.entity.vo.GroupStaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组-员工 Dao
 */
public interface GroupStaffDao {

    /**
     * 公司id
     *
     * @param companyId
     * @return
     */
    List<GroupStaffVO> getAllGroupAndStaff(int companyId);

    /**
     * 根据parentId获取某个组下的所有员工
     *
     * @param parentId
     * @return
     */
    List<GroupStaffVO> getGroupStaffListByParentIdAndCid(
            @Param("companyId") int companyId,
            @Param("parentId") String parentId);
}
