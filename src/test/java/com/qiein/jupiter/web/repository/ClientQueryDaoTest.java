package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.PlatPageVO;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: shiTao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientQueryDaoTest {

    @Autowired
    private ClientQueryDao clientQueryDao;

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;


    @Test
    public void testQueryDel() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCurrentPage(1);
        queryVO.setPageSize(10);
        queryVO.setTimeType(" UPDATETIME ");
        queryVO.setStart(1534377600);
        queryVO.setEnd(1537027199);
        PlatPageVO pageVO = clientQueryDao.queryDelClientInfo(queryVO);
        System.out.println(JSONObject.toJSONString(pageVO, true));
    }

    @Test
    public void test12312() {
        String sql = " SELECT  * FROM hm_pub_staff WHERE ID= :id";
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("id", 1);

        Map<String, Object> stringObjectMap = namedJdbc.queryForMap(sql, keyMap);

        List<Map<String, Object>> maps = namedJdbc.queryForList("SELECT * FROM hm_pub_staff", keyMap);
        System.out.println(stringObjectMap);
        System.out.println(JSON.toJSONString(maps));
    }


    @Test
    public void testClientQueryPage() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCurrentPage(1);
        queryVO.setPageSize(10);
        queryVO.setTimeType(" CREATETIME ");
        queryVO.setStart(1534377600);
        queryVO.setEnd(1537027199);
        queryVO.setUid(1);
        queryVO.setCompanyId(3);
        queryVO.setPmsLimit(0);
        queryVO.setLinkLimit("0");
        queryVO.setAction("all");
        queryVO.setRole("dscj");
        PlatPageVO pageVO = clientQueryDao.clientSearchPage(queryVO);
        System.out.println(JSONObject.toJSONString(pageVO));
    }

}