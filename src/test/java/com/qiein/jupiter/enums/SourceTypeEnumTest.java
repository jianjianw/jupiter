package com.qiein.jupiter.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: shiTao
 */
public class SourceTypeEnumTest {

    @Test
    public void switchName() {
        String s = SourceTypeEnum.switchName(1);
        System.out.println(s);
    }
}