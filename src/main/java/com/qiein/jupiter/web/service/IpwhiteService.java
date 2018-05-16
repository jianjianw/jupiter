package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.po.IpWhitePO;

public interface IpwhiteService {
	
	
	public void insert(IpWhitePO ipWhitePo);
	
	public void delete(int id);
	
	public List<IpWhitePO> select(int companyid);
	
	public void update(IpWhitePO ipWhitePo);
	
}
