package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GroupPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupDaoTest {

    @Autowired
    private GroupDao groupDao;

    @Test
    public void getList() {
        List<GroupPO> list = groupDao.findAllByCompanyId(1);
    }

}