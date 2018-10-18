package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.dto.ReportParamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ZjsDetailReport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String,Object> getZjsDetailReportByStaff(ReportParamDTO reportParamDTO){
        StringBuilder sb = new StringBuilder();
        sb.append("select *");
        String sql = sb.toString();
        jdbcTemplate.queryForList(sql);
        return null;

    }


}
