package com.qiein.jupiter.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.vo.ScreenVO;

/**
 * 大屏
 */
public interface ScreenDao extends BaseDao<ScreenVO> {
	/**
     * 大屏
     * @param companyId
     * @return
     */
    List<ScreenVO> getDayKZTotal(@Param("companyId") String companyId);

	List<ScreenVO> getMonthKZTotal(@Param("companyId") String companyId);

	List<ScreenVO> getDdNum(@Param("companyId") String companyId);

	List<ScreenVO> getWXFlag(@Param("companyId") String companyId);

	List<ScreenVO> getWXKzNum(@Param("companyId") String companyId);

	List<ScreenVO> getWXKzComeNum(@Param("companyId") String companyId);

	List<ScreenVO> getWXGroupKzNumMonth(@Param("companyId") String companyId);

	List<ScreenVO> getWXGroupKzComeNumWeek(@Param("companyId") String companyId);

	List<ScreenVO> getWXGroupKzComeNumMonth(@Param("companyId") String companyId);

	List<ScreenVO> getDayValidKZ(@Param("companyId") String companyId);

	List<ScreenVO> getDayComeKZ(@Param("companyId") String companyId);

	List<ScreenVO> getDaySuccessKZ(@Param("companyId") String companyId);

	List<ScreenVO> getDayValidKZcost(@Param("companyId") String companyId);

	List<ScreenVO> getDaySrcValidKZ(@Param("companyId") String companyId);

	List<ScreenVO> getDaySrcKZ(@Param("companyId") String companyId);

	List<ScreenVO> getDaySrcKZValideRate(@Param("companyId") String companyId);
}
