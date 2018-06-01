package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 员工交接VO
 * Created by Tt(叶华葳)
 * on 2018/5/14 0014.
 */
public class StaffChangeVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer operaId;
	private String operaName;
	private Integer companyId;
	private Integer toStaffId;
	private String toStaffName;
	private String groupId;
	private String groupName;
	private Integer oldStaffId;
	private String oldStaffName;

	public Integer getOperaId() {
		return operaId;
	}

	public void setOperaId(Integer operaId) {
		this.operaId = operaId;
	}

	public String getOperaName() {
		return operaName;
	}

	public void setOperaName(String operaName) {
		this.operaName = operaName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getToStaffId() {
		return toStaffId;
	}

	public void setToStaffId(Integer toStaffId) {
		this.toStaffId = toStaffId;
	}

	public String getToStaffName() {
		return toStaffName;
	}

	public void setToStaffName(String toStaffName) {
		this.toStaffName = toStaffName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getOldStaffId() {
		return oldStaffId;
	}

	public void setOldStaffId(Integer oldStaffId) {
		this.oldStaffId = oldStaffId;
	}

	public String getOldStaffName() {
		return oldStaffName;
	}

	public void setOldStaffName(String oldStaffName) {
		this.oldStaffName = oldStaffName;
	}
}
