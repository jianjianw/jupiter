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
    public List<DictionaryPO> getCompanyDicByType(int companyId, String dicType);

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

    /**
     * 添加流失原因
     *
     * @param companyId
     * @param dicName
     */
    public void addRunoffReason(int companyId, String dicName);

    /**
     * 编辑流失原因
     *
     * @param companyId
     * @param id
     * @param dicName
     */
    public void editRunoffReason(int companyId, int id, String dicName);

    /**
     * 获取自定义地点数据，没有则获取共有的数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getCommonDicByType(int companyId, String dicType);
}
