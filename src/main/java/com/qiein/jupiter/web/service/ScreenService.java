package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.vo.ScreenVO;

public interface ScreenService {
   
	List<ScreenVO> getDayKZTotal(String companyId);

	List<ScreenVO> getMonthKZTotal(String companyId);

	List<ScreenVO> getDdNum(String companyId);

	List<ScreenVO> getWXFlag(String companyId);

	List<ScreenVO> getWXKzNum(String companyId);

	List<ScreenVO> getWXKzComeNum(String companyId);

	List<ScreenVO> getWXGroupKzNumMonth(String companyId);

	List<ScreenVO> WXGroupKzComeNumWeek(String companyId);

	List<ScreenVO> WXGroupKzComeNumMonth(String companyId);

	List<ScreenVO> getDayValidKZ(String companyId);

	List<ScreenVO> getDayComeKZ(String companyId);

	List<ScreenVO> getDaySuccessKZ(String companyId);

	List<ScreenVO> getDayValidKZcost(String companyId);

	List<ScreenVO> getDaySrcValidKZ(String companyId);

	List<ScreenVO> getDaySrcKZ(String companyId);

	List<ScreenVO> getDaySrcKZValideRate(String companyId);
}
