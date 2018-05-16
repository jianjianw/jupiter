package com.qiein.jupiter.web.service;

import java.util.List;

import com.qiein.jupiter.web.entity.po.IpWhitePO;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
public interface IpwhiteService {
	
	/**
	 * 新增
	 *
	 * @param ipWhitePo
	 * @return
	 */
	public void insert(IpWhitePO ipWhitePo);
	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	public void delete(int id);
	
	/**
	 * 修改
	 *
	 * @param ipWhitePo
	 * @return
	 */
	public void update(IpWhitePO ipWhitePo);
	/**
	 * 显示页面
	 *
	 * @param companyId
	 * @return List<IpWhitePO>
	 */
	public List<IpWhitePO> get_all_ip_by_companyId(int companyId);
	
}
