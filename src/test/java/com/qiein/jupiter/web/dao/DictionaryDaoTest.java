package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.web.entity.vo.MenuVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class DictionaryDaoTest {
@Autowired
private DictionaryDao dictionaryDao;
    @Test
    public void getCompanyMemu() {
        List<MenuVO> list = dictionaryDao.getCompanyMemu(0,DictionaryConstant.MENU_TYPE);
        System.out.println(list.size());
    }
}