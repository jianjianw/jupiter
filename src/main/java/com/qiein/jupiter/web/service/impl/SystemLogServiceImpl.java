package com.qiein.jupiter.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.dao.SystemLogDao;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.SystemLogService;

@Service
public class SystemLogServiceImpl implements SystemLogService {

	@Autowired
	private SystemLogDao logDao;

	@Override
	public void addLog(SystemLog log) {
		logDao.addSystemLog(DBSplitUtil.getSystemLogTabName(log.getCompanyId()), log);
	}
}