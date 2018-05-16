package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.web.dao.IpWhiteDao;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.service.IpWhiteService;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
@Service
public class IpWhiteServiceImpl implements IpWhiteService {


	@Autowired
	private IpWhiteDao ipwhitedao;
	
	/**
	 * 新增
	 *
	 * @param ipWhitePo
	 * @return
	 */
	@Override
	public void insert(IpWhitePO ipWhitePo) {
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
		ipwhitedao.update(ipWhitePo);
	}
	/**
	 * 显示页面
	 *
	 * @param companyId
	 * @return List<IpWhitePO>
	 */
	@Override
	public List<IpWhitePO> getAllIpByCompanyId(int companyId) {
		return ipwhitedao.getAllIpByCompanyId(companyId);
	}

	
}
