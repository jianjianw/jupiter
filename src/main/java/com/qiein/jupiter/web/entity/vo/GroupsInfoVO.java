package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.StaffPO;

import java.util.List;

/**
 * 网销排班部门小组实体类
 * Created by Administrator on 2018/5/8 008.
 */
public class GroupsInfoVO extends GroupPO {

	private static final long serialVersionUID = 1L;

	/**
	 * 在线人数
	 */
	private Integer lineNum;

	/**
	 * 订单数
	 */
	private Integer orderNum;

	/**
	 * 是否有权限查看
	 */
	private boolean showFlag;

	/**
	 * 下属小组列表
	 */
	private List<GroupsInfoVO> groupList;

	/**
	 * 组内员工信息
	 */
	private List<StaffPO> staffList;

	@Override
	public String toString() {
		return "GroupsInfoVO{" +
				"groupId= "+super.getGroupId()+
				"lineNum=" + lineNum +
				", orderNum=" + orderNum +
				", showFlag=" + showFlag +
				", groupList=" + groupList +
				", staffList=" + staffList +
				'}';
	}

	public List<GroupsInfoVO> getGroupList() {
		return groupList;
	}

	public List<StaffPO> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<StaffPO> staffList) {
		this.staffList = staffList;
	}

	public void setGroupList(List<GroupsInfoVO> groupList) {
		this.groupList = groupList;
	}

	public Integer getLineNum() {
		return lineNum;
	}

	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public boolean isShowFlag() {
		return showFlag;
	}

	public void setShowFlag(boolean showFlag) {
		this.showFlag = showFlag;
	}
}
