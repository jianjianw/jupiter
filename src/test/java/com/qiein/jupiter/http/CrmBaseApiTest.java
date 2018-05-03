package com.qiein.jupiter.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CrmBaseApiTest {

    @Autowired
    private CrmBaseApi crmBaseApi;
    @Test
    public void testGet(){
    }
}