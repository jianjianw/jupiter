package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.vo.DictionaryVO;
import com.qiein.jupiter.web.entity.vo.MenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典Dao
 */
public interface DictionaryDao extends BaseDao<DictionaryPO> {

    /**
     * 获取企业左上角菜单栏
     *
     * @param companyId
     * @param dicType
     * @return
     */
    List<MenuVO> getCompanyMenu(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 根绝类型获取字典数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    List<DictionaryPO> getDicByType(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 根据类型，名称，获取字典数据
     *
     * @param companyId
     * @param dicType
     * @param dicName
     * @return
     */
    DictionaryPO getDicByTypeAndName(@Param("companyId") int companyId, @Param("dicType") String dicType, @Param("dicName") String dicName);

    /**
     * 根据字典code和type获取字典列表
     *
     * @param companyId
     * @param dicType
     * @param dicCodes
     * @return
     */
    List<DictionaryPO> getDicByCodeAndType(@Param("companyId") int companyId, @Param("dicType") String dicType, @Param("dicCodes") String[] dicCodes);

    /**
     * 批量删除字典数据
     *
     * @param companyId
     * @param idArr
     */
    void batchDeleteByIds(@Param("companyId") int companyId, @Param("idArr") String[] idArr);

    /**
     * 获取一个公司下面所有的字典信息
     */
    List<DictionaryPO> getDictByCompanyId(@Param("companyId") int companyId);

    /**
     * 获取最大编码
     *
     * @param companyId
     * @param dicType
     * @return
     */
    int getMaxDicCode(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 获取最大顺序
     *
     * @param companyId
     * @param dicType
     * @return
     */
    int getMaxPriority(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 新增字典记录
     *
     * @param dictionaryPO
     * @return
     */
    int createDict(DictionaryPO dictionaryPO);

    /**
     * 新增咨询类型
     *
     * @param dictionaryPO
     * @return
     */
    int createCommonType(DictionaryPO dictionaryPO);

    /**
     * 从默认咨询类型中获取记录添加到本公司中
     *
     * @param companyId
     * @param dicCodes
     * @return
     */
    int addCommonTypeFromZero(@Param("companyId") int companyId, @Param("dicCodes") String[] dicCodes);

    /**
     * 直接添加咨询类型
     *
     * @param dictionaryVO
     * @return
     */
    int addCommonType(DictionaryVO dictionaryVO);

    /**
     * 新增咨询类型
     *
     * @param dictionaryVO
     * @return
     */
    int createCommonType(DictionaryVO dictionaryVO);

    /**
     * 获取咨询类型最新排序
     *
     * @param companyId
     * @return
     */
    int getCommonTypePriority(@Param("companyId") Integer companyId);

    /**
     * 根据id删除
     *
     * @param id
     * @param companyId
     * @return
     */
    int delDict(@Param("id") int id, @Param("companyId") int companyId);

    /**
     * 编辑字典排序
     *
     * @param id
     * @param priority
     * @param companyId
     * @return
     */
    int editDictPriority(@Param("id") int id, @Param("priority") int priority, @Param("companyId") int companyId);

    /**
     * 编辑字典名称
     */
    int editDictName(DictionaryPO dictionaryPO);

    /**
     * 编辑字典可用状态
     */
    int editDictShowFlag(DictionaryPO dictionaryPO);

    /**
     * 根据类型，code，获取字典数据
     */
    int updateDictNameByTypeAndCode(DictionaryPO dictionaryPO);

    /**
     * 根据公司ID 和iD 获取
     */
    DictionaryPO getByCompanyIdAndId(@Param("companyId") int companyId, @Param("id") int id);
}
