package com.qiein.jupiter.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpUtilTest {

    @Test
    public void getRequestParam() {
    }

    @Test
    public void getRequestToken() {
    }

    @Test
    public void getHttpServletRequest() {
    }

    @Test
    public void getIpAddr() {
    }

    @Test
    public void isIp() {
        Assert.assertTrue(HttpUtil.isIp("192.168.3.1"));
        Assert.assertFalse(HttpUtil.isIp("192.1681.3.1"));
    }
}