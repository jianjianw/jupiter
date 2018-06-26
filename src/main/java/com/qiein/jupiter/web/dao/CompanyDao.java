package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;

import java.util.List;

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
     * @param ipLimit
     * @param companyId
     * @return
     */
    void editIpLimit(@Param("ipLimit") Integer ipLimit, @Param("companyId") int companyId);

    /**
     * 获取系统中的企业信息列表
     * @return
     */
	List<CompanyPO> listComp();
	
	/**
     * 编辑咨询类型
     * @return
     */
	void editTypeRepeat(@Param("b") Boolean b,@Param("companyId") Integer companyId);
	
	/**
     * 编辑渠道类型
     * @return
     */
	void editTypeSrcRepeat(@Param("b") boolean b, @Param("companyId") int companyId);

	void editKZStutas(@Param("statusIgnore") String statusIgnore, @Param("companyId") int companyId);

	void editKZday(@Param("timeTypeIgnore")String timeTypeIgnore, @Param("dayIgnore") String dayIgnore,@Param("companyId") int companyId);
}
