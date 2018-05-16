package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.vo.LazyWorkVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16 0016.
 */
public interface LazyWorkDao {
    /**
     * 根据员工编号获取员工怠工日志
     * @param staffId
     * @param companyId
     * @return
     */
    List<LazyWorkVO> getLazyWorkListByStaffId(@Param("staffId") int staffId,@Param("companyId") int companyId);
}
