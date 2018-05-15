package com.qiein.jupiter.util;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void nullToStrTrim() {
    }

    @Test
    public void ignoreCaseEqual() {
    }

    @Test
    public void isEmpty() {
        boolean nullStr = StringUtil.isEmpty("");
        System.out.println(nullStr);
    }

    @Test
    public void underscoreName() {
        String abcDde = StringUtil.underscoreName("abcDde");
        System.out.println(abcDde);
    }

    @Test
    public void camelCaseName() {
        String abcDde = StringUtil.camelCaseName("abc_dde_");
        System.out.println(abcDde);
    }
}