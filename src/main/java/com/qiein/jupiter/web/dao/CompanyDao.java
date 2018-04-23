package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import org.apache.ibatis.annotations.Param;

/**
 * 公司Dao
 */
public interface CompanyDao extends BaseDao<CompanyPO> {

    public CompanyVO getVOById(@Param("companyId") int companyId);
}
