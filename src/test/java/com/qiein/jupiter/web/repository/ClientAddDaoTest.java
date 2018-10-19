package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.web.entity.vo.ClientVO;
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
public class ClientAddDaoTest {

    @Autowired
    private ClientAddDao clientAddDao;


    @Test
    public void stesae() {
        clientAddDao.addPcZjsClientInfo();
//        clientAddDao.addGoldDataClientInfo();
    }

    @Test
    public void testAddDingClientInfo() {
        ClientVO clientVO = new ClientVO();
        clientVO.setCompanyId(2);
        clientVO.setOperaId(220);
        clientVO.setKzWechat("asdasdasdasddwqd");
        clientVO.setChannelId(43);
        clientVO.setSourceId(65);
        clientVO.setTypeId(1);
        clientVO.setZxStyle(1);
        clientAddDao.addDingClientInfo(clientVO);
    }
}