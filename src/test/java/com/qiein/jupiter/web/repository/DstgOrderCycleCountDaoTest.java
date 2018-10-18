package com.qiein.jupiter.web.repository;

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
public class DstgOrderCycleCountDaoTest {
    @Autowired
    private DstgOrderCycleCountDao dstgOrderCycleCountDao;

    @Test
    public void getCount() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCompanyId(2);
        queryVO.setStart(1534377600);
        queryVO.setEnd(1537027199);
        dstgOrderCycleCountDao.getCount(queryVO);
    }
}