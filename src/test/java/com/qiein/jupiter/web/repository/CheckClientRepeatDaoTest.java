package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.PlatAddClientInfoVO;
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
public class CheckClientRepeatDaoTest {

    @Autowired
    private CheckClientRepeatDao repeatDao;

    @Test
    public void check() {
        ClientVO clientInfoVO = new ClientVO();
        clientInfoVO.setCompanyId(2);
        clientInfoVO.setKzWechat("13213256555");

        clientInfoVO.setKzPhone("18699763304");
        clientInfoVO.setOperaName("涛涛");
        repeatDao.check(clientInfoVO);
    }
}