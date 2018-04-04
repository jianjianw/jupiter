package com.qiein.jupiter.web.service.serviceImpl;

import com.qiein.jupiter.web.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceImplTest {

    @Autowired
    private LoginService loginService;

    @Test
    public void login() {
        loginService.login("123", "14312");
    }

    @Test
    public void logout() {
        loginService.logout();
    }
}