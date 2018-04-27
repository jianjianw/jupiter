package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.DictionaryPO;

import java.util.List;

/**
 * 字典业务层
 */
public interface DictionaryService {
    /**
     * 根绝类型获取字典数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getDicByType(int companyId, String dicType);
}
