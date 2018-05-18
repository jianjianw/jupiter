package com.qiein.jupiter.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.dao.IpWhiteDao;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.entity.vo.IpWhitePageVO;
import com.qiein.jupiter.web.entity.vo.IpWhiteStaffVo;
import com.qiein.jupiter.web.entity.vo.IpWhiteStaffVoShow;
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
	 /**
     * 显示ip页面
     * @param companyId
     * @return List<IpWhiteStaffVo>
     */
	@Override
	public PageInfo<IpWhiteStaffVo> FindIpWhite(QueryMapDTO queryMapDTO, int companyId) {
		// TODO Auto-generated method stub
		PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
		List<IpWhiteStaffVo> list=ipwhitedao.FindIpWhite(companyId);
		return new PageInfo<>(list)  ;
	}
	
	/**
     * 根据公司id 寻找白名单ip
     * @param companyId
     * @return List<String>
     */
	@Override
	public List<String> findIp(int companyId) {
		// TODO Auto-generated method stub
		return ipwhitedao.findIp(companyId);
	}

	
}
