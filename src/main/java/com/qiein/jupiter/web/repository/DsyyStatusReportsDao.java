package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DsyyStatusReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: yyx
 * @Date: 2018-8-11
 */
@Repository
public class DsyyStatusReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取电商邀约状态报表
     * */
    public List<DsyyStatusReportsVO> getDsyyStatusReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig){
        //获取状态列表

        //获取小组列表



        return null;
    }
}
