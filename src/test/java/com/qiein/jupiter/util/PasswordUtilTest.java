package com.qiein.jupiter.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void isSimplePassword() {
        Assert.assertTrue(PasswordUtil.isSimplePassword("e10adc3949ba59abbe56e057f20f883e"));
    }
}