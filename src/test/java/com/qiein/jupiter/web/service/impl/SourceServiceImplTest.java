package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.dao.SourceDao;

/**
 * Created by Tt(叶华葳)
 * on 2018/5/25 0025.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SourceServiceImplTest {

    @Autowired
    private SourceDao sourceDao;

    @Test
    public void datDelSrc() throws Exception {
//       // int i=sourceDao.datDelCheck("1,2,3".split(","),"hm_crm_client_info_1");
//        System.out.println(i);
    }

}