package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.entity.vo.MenuVO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典业务层
 */
@Service
public class DictionaryServiceOmpl implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;


}
