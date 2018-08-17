package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
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
public class CwMonthOrderCountReportsTest {

    @Autowired
    private CwMonthOrderCountReportsDao cwMonthOrderCountReportsDao;

    @Test
    public void getCwMonthOrderCountReports() {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setCompanyId(2);
        reportsParamVO.setMonth("2018-08");
        cwMonthOrderCountReportsDao.getCwMonthOrderCountReports(reportsParamVO);
    }
}