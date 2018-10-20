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
	public List<ScreenVO> getDayKZTotal() {
		List<ScreenVO> dayKZTotal = screenDao.getDayKZTotal();
		return dayKZTotal;
	}

	@Override
	public List<ScreenVO> getMonthKZTotal() {
		List<ScreenVO> dayKZTotal = screenDao.getMonthKZTotal();
		return dayKZTotal;
	}

	@Override
	public List<ScreenVO> getDdNum() {
		List<ScreenVO> ddNum = screenDao.getDdNum();
		return ddNum;
	}

	@Override
	public List<ScreenVO> getWXFlag() {
		List<ScreenVO> wXFlag = screenDao.getWXFlag();
		return wXFlag;
	}

	@Override
	public List<ScreenVO> getWXKzNum() {
		List<ScreenVO> wXKzNum = screenDao.getWXKzNum();
		return wXKzNum;
	}

	@Override
	public List<ScreenVO> getWXKzComeNum() {
		List<ScreenVO> wXKzComeNum = screenDao.getWXKzComeNum();
		return wXKzComeNum;
	}

	@Override
	public List<ScreenVO> getWXGroupKzNumMonth() {
		List<ScreenVO> wXGroupKzNumMonth = screenDao.getWXGroupKzNumMonth();
		return wXGroupKzNumMonth;
	}

	@Override
	public List<ScreenVO> WXGroupKzComeNumWeek() {
		List<ScreenVO> wXGroupKzComeNumWeek = screenDao.getWXGroupKzComeNumWeek();
		return wXGroupKzComeNumWeek;
	}

	@Override
	public List<ScreenVO> WXGroupKzComeNumMonth() {
		List<ScreenVO> wXGroupKzComeNumMonth = screenDao.getWXGroupKzComeNumMonth();
		return wXGroupKzComeNumMonth;
	}

	@Override
	public List<ScreenVO> getDayValidKZ() {
		List<ScreenVO> dayValidKZ = screenDao.getDayValidKZ();
		return dayValidKZ;
	}

	@Override
	public List<ScreenVO> getDayComeKZ() {
		List<ScreenVO> dayComeKZ = screenDao.getDayComeKZ();
		return dayComeKZ;
	}

	@Override
	public List<ScreenVO> getDaySuccessKZ() {
		List<ScreenVO> daySuccessKZ = screenDao.getDaySuccessKZ();
		return daySuccessKZ;
	}

	@Override
	public List<ScreenVO> getDayValidKZcost() {
		List<ScreenVO> dayValidKZcost = screenDao.getDayValidKZcost();
		return dayValidKZcost;
	}

	@Override
	public List<ScreenVO> getDaySrcValidKZ() {
		List<ScreenVO> daySrcValidKZ = screenDao.getDaySrcValidKZ();
		return daySrcValidKZ;
	}

	@Override
	public List<ScreenVO> getDaySrcKZ() {
		List<ScreenVO> daySrcKZ = screenDao.getDaySrcKZ();
		return daySrcKZ;
	}

	@Override
	public List<ScreenVO> getDaySrcKZValideRate() {
		List<ScreenVO> daySrcKZValideRate = screenDao.getDaySrcKZValideRate();
		return daySrcKZValideRate;
	}

	
}
