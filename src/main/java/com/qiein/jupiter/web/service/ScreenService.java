package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.vo.ScreenVO;

public interface ScreenService {
   
	List<ScreenVO> getDayKZTotal();

	List<ScreenVO> getMonthKZTotal();

	List<ScreenVO> getDdNum();

	List<ScreenVO> getWXFlag();

	List<ScreenVO> getWXKzNum();

	List<ScreenVO> getWXKzComeNum();

	List<ScreenVO> getWXGroupKzNumMonth();

	List<ScreenVO> WXGroupKzComeNumWeek();

	List<ScreenVO> WXGroupKzComeNumMonth();

	List<ScreenVO> getDayValidKZ();

	List<ScreenVO> getDayComeKZ();

	List<ScreenVO> getDaySuccessKZ();

	List<ScreenVO> getDayValidKZcost();

	List<ScreenVO> getDaySrcValidKZ();

	List<ScreenVO> getDaySrcKZ();

	List<ScreenVO> getDaySrcKZValideRate();
}
