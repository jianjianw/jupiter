package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.StaffStatusLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工状态日志
 *
 * @author shiTao
 */
public interface StaffStatusLogDao {
    /**
     * 新增
     *
     * @return
     */
    int insert(StaffStatusLog staffStatusLog);

    /**
     * 根据员工Id获取日志,获取一天之内的
     *
     * @return
     */
    List<StaffStatusLog> listByStaffId(@Param("companyId") int companyId,
                                       @Param("staffId") int staffId, @Param("time") int time);
}
