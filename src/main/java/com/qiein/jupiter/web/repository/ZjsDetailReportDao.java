package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.dto.ReportParamDTO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.ZjsClientDetailReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ZjsDetailReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 转介绍报表详情，按客服组汇总
     *
     */
    public Map<String,Object> getZjsDetailReportByGroup(ReportsParamVO reportsParamVO){
        List<ZjsClientDetailReportVO> zjsClientDetailReportVOList = new ArrayList<ZjsClientDetailReportVO>();

        //获得总客资

        //获取筛选待定

        //获取筛选中

        //获取筛选无效

        //jdbcTemplate.queryForList(sql);
        return null;

    }

    // 获取总客资
    private void getTotalClientCount(){

    }

    //获取筛选待定

    //获取筛选中

    //获取筛选无效





}
