package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import org.apache.ibatis.annotations.Param;

/**
 * 公司Dao
 */
public interface CompanyDao extends BaseDao<CompanyPO> {

    CompanyVO getVOById(@Param("companyId") int companyId);

    /**
     * 编辑自定义设置
     *
     * @param column
     * @param flag
     * @return
     */
    int updateFlag(@Param("companyId") int companyId, @Param("column") String column,
                   @Param("flag") boolean flag);

    /**
     * 编辑自定义范围
     *
     * @param column
     * @param num
     * @return
     */
    int updateRange(@Param("companyId") int companyId, @Param("column") String column, @Param("num") int num);
    /**
	 * 获取iplimt
	 * 
	 * @param companyId
	 * @return
	 */
	int getIpLimit(int companyId);
	/**
	 * 修改iplimt
	 * 
	 * @param iplimit
	 * * @param companyId
	 * @return
	 */
	void editIpLimit(Integer iplimit, int companyId);
}
