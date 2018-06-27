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
	 * 更改咨询类型(客资校验是否忽略咨询类型)
	 * @return
	 */
	void editTypeRepeat(@Param("b") Boolean b,@Param("companyId") Integer companyId);
	
	/**
	 * 更改渠道类型(客资校验是否忽略渠道类型)
	 * @return
	 */
	void editTypeSrcRepeat(@Param("b") boolean b, @Param("companyId") int companyId);

	/**
	 * 更改客资录入时间和最后操作时间,客资状态是否可以重复录
	 * @return
	 */
	void editKZday(@Param("statusIgnore") String statusIgnore,@Param("timeTypeIgnore")String timeTypeIgnore, @Param("dayIgnore") int dayIgnore,@Param("companyId") int companyId);
	/**
	 * 查询哪些客资重复被拦截
	 * @return
	 */
	CompanyPO selectAll(@Param("companyId") int companyId);
}
