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
    public List<MenuVO> getCompanyMemu(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 根绝类型获取字典数据
     *
     * @param companyId
     * @param dicType
     * @return
     */
    public List<DictionaryPO> getDicByType(@Param("companyId") int companyId, @Param("dicType") String dicType);

    /**
     * 根据类型，名称，获取字典数据
     *
     * @param companyId
     * @param dicType
     * @param dicName
     * @return
     */
    public DictionaryPO getDicByTypeAndName(@Param("companyId") int companyId, @Param("dicType") String dicType, @Param("dicName") String dicName);
}
