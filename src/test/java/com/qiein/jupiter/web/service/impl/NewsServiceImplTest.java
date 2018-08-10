package com.qiein.jupiter.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.entity.po.NewsPO;
import com.qiein.jupiter.web.service.NewsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceImplTest {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private NewsService newsService;

    @Test
    public void getAllList() {
        List<NewsPO> list = new ArrayList<>();
        NewsPO newsPO = new NewsPO();
        newsPO.setCompanyId(2);
        newsPO.setHead("公司2");
        newsPO.setStaffId(123);
        newsPO.setType("11");
        list.add(newsPO);
        NewsPO newsPO2 = new NewsPO();
        newsPO2.setCompanyId(1);
        newsPO2.setHead("公司1");
        newsPO2.setStaffId(131);
        newsPO2.setType("22");
        list.add(newsPO2);
        newsDao.batchInsertNews(list);
    }

    @Test
    public void getNotReadList() {
    }

    @Test
    public void batchUpdateNewsReadFlag() {
    }

    @Test
    public void getNewsTotalAmountAndFlag() {
        newsService.getNewsTotalAmountAndFlag(1, 1);
    }
}