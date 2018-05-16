package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.web.dao.IpwhiteDao;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.service.IpwhiteService;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
@Service
public class IpwhiteServiceImpl implements IpwhiteService {


	@Autowired
	private IpwhiteDao ipwhitedao;
	
	/**
	 * 新增
	 *
	 * @param ipWhitePo
	 * @return
	 */
	@Override
	public void insert(IpWhitePO ipWhitePo) {
		// TODO Auto-generated method stub
		ipwhitedao.insert(ipWhitePo);
	}
	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		ipwhitedao.delete(id);
	}

	/**
	 * 修改
	 *
	 * @param ipWhitePo
	 * @return
	 */
	@Override
	public void update(IpWhitePO ipWhitePo) {
		// TODO Auto-generated method stub
		ipwhitedao.update(ipWhitePo);
	}
	/**
	 * 显示页面
	 *
	 * @param companyId
	 * @return List<IpWhitePO>
	 */
	@Override
	public List<IpWhitePO> get_all_ip_by_companyId(int companyId) {
		// TODO Auto-generated method stub
		return ipwhitedao.get_all_ip_by_companyId(companyId);
	}

	
}
