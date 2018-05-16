package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.po.IpWhitePO;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
public interface IpwhiteService {
	
	
	public void insert(IpWhitePO ipWhitePo);
	
	public void delete(int id);
	
	
	public void update(IpWhitePO ipWhitePo);

	public List<IpWhitePO> get_all_ip_by_companyId(int companyId);
	
}
