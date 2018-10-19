package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.repository.*;
import com.qiein.jupiter.web.service.ReportService;
import com.qiein.jupiter.web.service.ScreenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 报表
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

	
}
