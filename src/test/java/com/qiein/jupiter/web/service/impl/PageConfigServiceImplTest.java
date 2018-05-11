package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.service.PageConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageConfigServiceImplTest {

    @Autowired
    private PageConfigService pageConfigService;

    @Test
    public void listPageConfigByCidAndRole() {
    }

    @Test
    public void getAllPageFilterMap() {
        pageConfigService.getPageFilterMap(0, "");
    }
}