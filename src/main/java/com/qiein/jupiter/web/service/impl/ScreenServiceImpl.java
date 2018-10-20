package com.qiein.jupiter.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qiein.jupiter.web.dao.ScreenDao;
import com.qiein.jupiter.web.entity.vo.ScreenVO;
import com.qiein.jupiter.web.service.ScreenService;

/**
 * 金夫人大屏
 */
@Service
public class ScreenServiceImpl implements ScreenService {

    @Autowired
    private ScreenDao screenDao;

	@Override
	public List<ScreenVO> getDayKZTotal(String companyId) {
		List<ScreenVO> dayKZTotal = screenDao.getDayKZTotal(companyId);
		return dayKZTotal;
	}

	@Override
	public List<ScreenVO> getMonthKZTotal(String companyId) {
		List<ScreenVO> dayKZTotal = screenDao.getMonthKZTotal(companyId);
		return dayKZTotal;
	}

	@Override
	public List<ScreenVO> getDdNum(String companyId) {
		List<ScreenVO> ddNum = screenDao.getDdNum(companyId);
		return ddNum;
	}

	@Override
	public List<ScreenVO> getWXFlag(String companyId) {
		List<ScreenVO> wXFlag = screenDao.getWXFlag(companyId);
		return wXFlag;
	}

	@Override
	public List<ScreenVO> getWXKzNum(String companyId) {
		List<ScreenVO> wXKzNum = screenDao.getWXKzNum(companyId);
		return wXKzNum;
	}

	@Override
	public List<ScreenVO> getWXKzComeNum(String companyId) {
		List<ScreenVO> wXKzComeNum = screenDao.getWXKzComeNum(companyId);
		return wXKzComeNum;
	}

	@Override
	public List<ScreenVO> getWXGroupKzNumMonth(String companyId) {
		List<ScreenVO> wXGroupKzNumMonth = screenDao.getWXGroupKzNumMonth(companyId);
		return wXGroupKzNumMonth;
	}

	@Override
	public List<ScreenVO> WXGroupKzComeNumWeek(String companyId) {
		List<ScreenVO> wXGroupKzComeNumWeek = screenDao.getWXGroupKzComeNumWeek(companyId);
		return wXGroupKzComeNumWeek;
	}

	@Override
	public List<ScreenVO> WXGroupKzComeNumMonth(String companyId) {
		List<ScreenVO> wXGroupKzComeNumMonth = screenDao.getWXGroupKzComeNumMonth(companyId);
		return wXGroupKzComeNumMonth;
	}

	@Override
	public List<ScreenVO> getDayValidKZ(String companyId) {
		List<ScreenVO> dayValidKZ = screenDao.getDayValidKZ(companyId);
		return dayValidKZ;
	}

	@Override
	public List<ScreenVO> getDayComeKZ(String companyId) {
		List<ScreenVO> dayComeKZ = screenDao.getDayComeKZ(companyId);
		return dayComeKZ;
	}

	@Override
	public List<ScreenVO> getDaySuccessKZ(String companyId) {
		List<ScreenVO> daySuccessKZ = screenDao.getDaySuccessKZ(companyId);
		return daySuccessKZ;
	}

	@Override
	public List<ScreenVO> getDayValidKZcost(String companyId) {
		List<ScreenVO> dayValidKZcost = screenDao.getDayValidKZcost(companyId);
		return dayValidKZcost;
	}

	@Override
	public List<ScreenVO> getDaySrcValidKZ(String companyId) {
		List<ScreenVO> daySrcValidKZ = screenDao.getDaySrcValidKZ(companyId);
		return daySrcValidKZ;
	}

	@Override
	public List<ScreenVO> getDaySrcKZ(String companyId) {
		List<ScreenVO> daySrcKZ = screenDao.getDaySrcKZ(companyId);
		return daySrcKZ;
	}

	@Override
	public List<ScreenVO> getDaySrcKZValideRate(String companyId) {
		List<ScreenVO> daySrcKZValideRate = screenDao.getDaySrcKZValideRate(companyId);
		return daySrcKZValideRate;
	}

	
}
