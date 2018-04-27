package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.vo.MenuVO;
import com.qiein.jupiter.web.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.ListUI;
import java.util.Collections;
import java.util.List;

/**
 * 字典业务层
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;

    /**
     * 根绝类型获取字典数据,先获取企业自定义，没有则获取共有的
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getDicByType(int companyId, String dicType) {
        List<DictionaryPO> list = dictionaryDao.getDicByType(companyId, dicType);
        if (CollectionUtils.isEmpty(list)) {
            list = dictionaryDao.getDicByType(DictionaryConstant.COMMON_COMPANYID, dicType);
        }
        return list;
    }

}
