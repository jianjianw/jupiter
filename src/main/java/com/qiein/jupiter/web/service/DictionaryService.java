package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 新增流失原因
     *
     * @param dictionaryPO
     */
    public void addInvalidReason(DictionaryPO dictionaryPO);

    /**
     * 编辑流失原因
     *
     * @param dictionaryPO
     */
    public void editInvalidReason(DictionaryPO dictionaryPO);

    /**
     * 批量删除字典数据
     *
     * @param companyId
     * @param ids
     */
    public void batchDeleteByIds(int companyId, String ids);
}
