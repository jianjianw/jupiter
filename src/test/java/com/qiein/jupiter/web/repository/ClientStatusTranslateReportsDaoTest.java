package com.qiein.jupiter.web.repository;

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
public class ClientStatusTranslateReportsDaoTest {

    @Autowired
    private ClientStatusTranslateReportsDao reportsDao;

    @Test
    public void getClientStatusTranslateForGroup() {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setCompanyId(2);
        reportsParamVO.setStart(1533052800);
        reportsParamVO.setEnd(1535731199);
//        "0-99-145" -> " size = 4"
//        "0-93-94" -> " size = 7"
        reportsParamVO.setGroupIds("0-99-145,0-93-94");
        reportsDao.getClientStatusTranslateForGroup(reportsParamVO);
    }

    @Test
    public void getClientStatusTranslateForGroupStaff() {

        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setCompanyId(2);
        reportsParamVO.setStart(1533052800);
        reportsParamVO.setEnd(1535731199);
        reportsParamVO.setGroupId("0-99-100");
        reportsDao.getClientStatusTranslateForGroupStaff(reportsParamVO);
    }
}