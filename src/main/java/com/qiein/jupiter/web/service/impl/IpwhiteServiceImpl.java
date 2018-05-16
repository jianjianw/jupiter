package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.web.dao.IpwhiteDao;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.service.IpwhiteService;
@Service
public class IpwhiteServiceImpl implements IpwhiteService {


	@Autowired
	private IpwhiteDao ipwhitedao;
	@Override
	public void insert(IpWhitePO ipWhitePo) {
		// TODO Auto-generated method stub
		ipwhitedao.insert(ipWhitePo);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		ipwhitedao.delete(id);
	}

	@Override
	public List<IpWhitePO> select(int companyid) {
		// TODO Auto-generated method stub
//		return ipwhitedao.;
		return null;
	}

	@Override
	public void update(IpWhitePO ipWhitePo) {
		// TODO Auto-generated method stub
		ipwhitedao.update(ipWhitePo);
	}

	
}
