package com.qiein.jupiter.web.service;

import java.util.List;
import java.util.Map;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.vo.DictionaryVO;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 新增字典记录
     *
     * @param dictionaryPO
     */
    void createDict(DictionaryPO dictionaryPO);

//    /**
//     * 编辑字典记录
//     * @param dictionaryPO
//     */
//    void editDict(DictionaryPO dictionaryPO);

    /**
     * 根据Id删除字典记录
     *
     * @param id
     * @param companyId
     */
    void delDict(int id, int companyId);

    /**
     * 编辑咨询类型接口
     *
     * @param dictionaryVO
     */
    void addCommonType(DictionaryVO dictionaryVO);

    /**
     * 根据类型和CODE 修改字典名称
     */
    int updateDictNameByTypeAndCode(DictionaryPO dictionaryPO);

    /**
     * 调换字典的排序
     *
     * @param id1
     * @param priority1
     * @param id2
     * @param priority2
     */
    void editDictPriority(int id1, int priority1, int id2, int priority2, int companyId);

    /**
     * 编辑字典名称
     */
    void editDictName(DictionaryPO dictionaryPO);

    /**
     * 字典排序
     */
    void editDictShowFlag(DictionaryPO dictionaryPO);
    /**
     * 根据公司ID 和iD 获取
     */
    DictionaryPO getByCompanyIdAndId(int companyId,int id);
    /**
     * 根据ids 批量获取
     */
    List<DictionaryPO> getByIds(String ids);

}
