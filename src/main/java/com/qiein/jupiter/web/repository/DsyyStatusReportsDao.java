package com.qiein.jupiter.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author: yyx
 * @Date: 2018-8-11
 */
@Repository
public class DsyyStatusReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


}
