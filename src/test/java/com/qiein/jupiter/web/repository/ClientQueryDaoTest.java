package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.QueryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author: shiTao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientQueryDaoTest {

    @Autowired
    private ClientQueryDao clientQueryDao;


    @Test
    public void testQueryDel() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCurrentPage(1);
        queryVO.setPageSize(10);
        queryVO.setTimeType(" UPDATETIME ");
        queryVO.setStart(1534377600);
        queryVO.setEnd(1537027199);
        clientQueryDao.queryDelClientInfo(queryVO, 3);
    }

}