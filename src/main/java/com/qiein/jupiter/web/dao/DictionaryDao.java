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
    List<MenuVO> getCompanyMemu(@Param("companyId") int companyId, @Param("dicType") String dicType);

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

    List<DictionaryPO> getDicByCodeAndType(@Param("companyId") int companyId,@Param("dicType") String dicType , @Param("dicCodes")String[] dicCodes);

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
     * @param companyId
     * @param dicType
     * @return
     */
    int getMaxDicCode(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 新增字典记录
     * @param dictionaryPO
     * @return
     */
    int createDict(DictionaryPO dictionaryPO);

    /**
     *  新增咨询类型
     * @param dictionaryVO
     * @return
     */
    int createCommonType(DictionaryVO dictionaryVO);

//    /**
//     * 编辑字典记录
//     * @param dictionaryPO
//     * @return
//     */
//    int editDict(DictionaryPO dictionaryPO);

    /**
     * 根据id删除
     * @param id
     * @param companyId
     * @return
     */
    int delDict(@Param("id") int id,@Param("companyId")int companyId);

    /**
     * 编辑字典排序
     * @param id
     * @param priority
     * @param companyId
     * @return
     */
    int editDictPriority(@Param("id") int id , @Param("priority") int priority , @Param("companyId") int companyId);
}
