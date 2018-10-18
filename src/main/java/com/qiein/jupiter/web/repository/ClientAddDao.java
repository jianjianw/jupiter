package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.exception.RException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 客资新增
 *
 * @Author: shiTao
 */
@Repository
public class ClientAddDao {

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * Pc端录入 电商客资
     *
     * @return
     */
    public String addPcDsClientInfo() {
        return null;
    }

    /**
     * PC端录入转介绍客资
     */
    @Transactional
    public void addPcZjsClientInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nickName", "小明1");
        paramMap.put("userName", "小明1");
        paramMap.put("phone", 123123123);

        namedJdbc.update("INSERT INTO  test (NICKNAME,PHONE,USERNAME ) VALUES ( :nickName,:phone,:userName)",paramMap);
        addGoldDataClientInfo();

    }

    /**
     * 金数据客资录入
     */
//    @Transactional
    public void addGoldDataClientInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nickName", "小明2");
        paramMap.put("userName", "小明2");
        paramMap.put("phone", 123123123);

        namedJdbc.update("INSERT INTO  test (NICKNAME,PHONE,USERNAME ) VALUES ( :nickName,:phone,:userName)",paramMap);

        throw new RException();
    }


    /**
     * 录入门市客资
     */
    public void addPcMdClientInfo() {

    }


    /**
     * 钉钉手机录入客资
     */
    public void adddDingClientInfo() {

    }


}
