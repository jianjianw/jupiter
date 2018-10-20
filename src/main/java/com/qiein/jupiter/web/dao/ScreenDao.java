package com.qiein.jupiter.web.dao;

import java.util.List;

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
    List<ScreenVO> getDayKZTotal();

	List<ScreenVO> getMonthKZTotal();

	List<ScreenVO> getDdNum();

	List<ScreenVO> getWXFlag();

	List<ScreenVO> getWXKzNum();

	List<ScreenVO> getWXKzComeNum();

	List<ScreenVO> getWXGroupKzNumMonth();

	List<ScreenVO> getWXGroupKzComeNumWeek();

	List<ScreenVO> getWXGroupKzComeNumMonth();

	List<ScreenVO> getDayValidKZ();

	List<ScreenVO> getDayComeKZ();

	List<ScreenVO> getDaySuccessKZ();

	List<ScreenVO> getDayValidKZcost();

	List<ScreenVO> getDaySrcValidKZ();

	List<ScreenVO> getDaySrcKZ();

	List<ScreenVO> getDaySrcKZValideRate();
}
