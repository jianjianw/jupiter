package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

import java.util.List;

public class CompanyVO extends BaseEntity {

	private static final long serialVersionUID = 4068121917474419086L;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 公司类型
	 */
	private int typeId;
	/**
	 * logo
	 */
	private String logo;

	/**
	 * 非本人客资脱敏显示
	 */
	private boolean notSelfBlind;

	/**
	 * 个人中心不能自己上下线
	 */
	private boolean unableSelfLine;

	/**
	 * 电商录入时不能直接指定客服
	 */
	private boolean unableAppointor;

	/**
	 * 电商客资超时不能返无效
	 */
	private int unableInvalidRange;

	/**
	 * 企业左上角菜单栏
	 */
	private List<MenuVO> menuList;

	public boolean isNotSelfBlind() {
		return notSelfBlind;
	}

	public void setNotSelfBlind(boolean notSelfBlind) {
		this.notSelfBlind = notSelfBlind;
	}

	public boolean isUnableSelfLine() {
		return unableSelfLine;
	}

	public void setUnableSelfLine(boolean unableSelfLine) {
		this.unableSelfLine = unableSelfLine;
	}

	public boolean isUnableAppointor() {
		return unableAppointor;
	}

	public void setUnableAppointor(boolean unableAppointor) {
		this.unableAppointor = unableAppointor;
	}

	public int getUnableInvalidRange() {
		return unableInvalidRange;
	}

	public void setUnableInvalidRange(int unableInvalidRange) {
		this.unableInvalidRange = unableInvalidRange;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<MenuVO> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuVO> menuList) {
		this.menuList = menuList;
	}
}
