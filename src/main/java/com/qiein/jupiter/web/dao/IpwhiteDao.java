package com.qiein.jupiter.web.dao;

import java.util.List;

import com.qiein.jupiter.web.entity.po.IpWhitePO;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
public interface IpwhiteDao extends BaseDao<IpWhitePO>{

	List<IpWhitePO> get_all_ip_by_companyId(int companyId);
	
	
}
