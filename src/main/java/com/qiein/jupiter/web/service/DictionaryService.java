package com.qiein.jupiter.web.service;

import java.util.List;
import java.util.Map;

import com.qiein.jupiter.web.entity.po.DictionaryPO;

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
    List<DictionaryPO> getCompanyDicByType(int companyId, String dicType);

    /**
     * 新增流失原因
     *
     * @param dictionaryPO
     */
    void addInvalidReason(DictionaryPO dictionaryPO);

    /**
     * 编辑流失原因
     *
     * @param dictionaryPO
     */
    void editInvalidReason(DictionaryPO dictionaryPO);

    /**
     * 批量删除字典数据
     *
     * @param companyId
     * @param ids
     */
    void batchDeleteByIds(int companyId, String ids);

    /**
     * 添加流失原因
     *
     * @param companyId
     * @param dicName
     */
    void addRunoffReason(int companyId, String dicName);

    /**
     * 编辑流失原因
     *
     * @param companyId
     * @param id
     * @param dicName
     */
    void editRunoffReason(int companyId, int id, String dicName);

    /**
     * 获取自定义地点数据，没有则获取共有的数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    List<DictionaryPO> getCommonDicByType(int companyId, String dicType);

    /**
     * 获取一个公司下的所有字典Map
     *
     * @param companyId
     * @return
     */
    Map<String, List<DictionaryPO>> getDictMapByCid(int companyId);
}
