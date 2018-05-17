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
	private List<IpWhiteStaffVo> list;

	public List<IpWhiteStaffVo> getList() {
		return list;
	}

	public void setList(List<IpWhiteStaffVo> list) {
		this.list = list;
	}
	
	
}
