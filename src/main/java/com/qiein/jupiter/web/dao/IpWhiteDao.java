package com.qiein.jupiter.web.dao;

import java.util.List;

import com.qiein.jupiter.web.entity.po.IpWhitePO;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
public interface IpWhiteDao extends BaseDao<IpWhitePO>{

	List<IpWhitePO> getAllIpByCompanyId(int companyId);
	
	
}
