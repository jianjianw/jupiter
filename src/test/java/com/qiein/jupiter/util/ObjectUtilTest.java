package com.qiein.jupiter.util;

import com.qiein.jupiter.web.entity.po.StaffPO;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectUtilTest {

    @Test
    public void reflect() {
        StaffPO staffPO=new StaffPO();
        staffPO.setUserName("    123              ");
        staffPO.setPhone("1231231231231231");
        try {
            ObjectUtil.reflect(staffPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void objectStrParamTrim() throws Exception {
        StaffPO staffPO=new StaffPO();
        staffPO.setUserName("    123              ");
        staffPO.setPhone("1231231231231231");
        ObjectUtil.objectStrParamTrim(staffPO);
        ObjectUtil.reflect(staffPO);
    }
}