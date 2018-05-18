package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
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
}
