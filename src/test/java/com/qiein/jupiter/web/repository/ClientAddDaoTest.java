package com.qiein.jupiter.web.repository;

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
}