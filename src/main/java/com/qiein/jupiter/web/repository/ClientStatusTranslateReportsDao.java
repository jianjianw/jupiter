package com.qiein.jupiter.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * （电商客服）客资各个状态转化统计
 *
 * @Author: shiTao
 */
@Repository
public class ClientStatusTranslateReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


}
