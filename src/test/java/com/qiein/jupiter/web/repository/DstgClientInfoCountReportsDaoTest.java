package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
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
public class DstgClientInfoCountReportsDaoTest {

    @Autowired
    private DstgClientInfoCountReportsDao dstgClientInfoCountReportsDao;

    @Test
    public void getDstgClientInfoCountReports() {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setCompanyId(2);
        reportsParamVO.setStart(1533052800);
        reportsParamVO.setEnd(1535731199);
        DsInvalidVO dsInvalidVO = new DsInvalidVO();
        dsInvalidVO.setDdIsValid(true);
        dsInvalidVO.setDsDdStatus("");
        dsInvalidVO.setDsInvalidLevel("");
        dsInvalidVO.setDsInvalidStatus("");
        dstgClientInfoCountReportsDao.getDstgClientInfoCountReports(reportsParamVO, dsInvalidVO);
    }
}