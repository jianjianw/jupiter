package com.qiein.jupiter.util;

import org.junit.Assert;
import org.junit.Test;

public class LoginCheckUtilTest {

    @Test
    public void isSimplePassword() {
        Assert.assertTrue(LoginCheckUtil.isSimplePassword("e10adc3949ba59abbe56e057f20f883e"));
    }
}