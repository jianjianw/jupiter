package com.qiein.jupiter.web.entity.vo;

import java.util.List;
/**
 * ip白名单员工信息页面
 *
 * @Author: XiangLiang
 */
public class IpWhiteStaffVoShow {
	
	   /**
     * ip白名单员工名单
     */
	private List<IpWhiteStaffVO> list;

	public List<IpWhiteStaffVO> getList() {
		return list;
	}

	public void setList(List<IpWhiteStaffVO> list) {
		this.list = list;
	}
	
	
}
