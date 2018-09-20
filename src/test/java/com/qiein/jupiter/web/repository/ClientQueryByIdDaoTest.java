package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: shiTao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientQueryByIdDaoTest {

    @Autowired
    private ClientQueryByIdDao clientQueryByIdDao;

    @Test
    public void getClientByKzid() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCompanyId(3);
        queryVO.setKzId("a10fcf31dfbdbd703686c43b7196a5df");
        queryVO.setUid(1);
        JSONObject clientByKzid = clientQueryByIdDao.getClientByKzid(queryVO);
        System.out.println(clientByKzid.toJSONString());
    }
}