package com.qiein.jupiter.web.entity.vo;
/**
 * ip白名单员工信息页面
 *
 * @Author: XiangLiang
 */
public class IpWhiteStaffVo {
	 /**
     * 员工id
     */
	private int id;
	 /**
     * 头像地址
     */
	private String imgAdd;
	 /**
     * 员工名称
     */
	private String staffName;
	 /**
     * 员工电话
     */
	private String phone;
	 /**
     * 人事关注
     */
	private String groupName;
	 /**
     * 账号创建时间
     */
	private  int createTime;
	 /**
     * 最后登陆时间
     */
	private int lastLoginTime;
	 /**
     * 最后登录ip
     */
	private String ipLocation;
	 /**
     * 最后登陆地址
     */
	private String finalAdd;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImgAdd() {
		return imgAdd;
	}
	public void setImgAdd(String imgAdd) {
		this.imgAdd = imgAdd;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public int getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getIpLocation() {
		return ipLocation;
	}
	public void setIpLocation(String ipLocation) {
		this.ipLocation = ipLocation;
	}
	public String getFinalAdd() {
		return finalAdd;
	}
	public void setFinalAdd(String finalAdd) {
		this.finalAdd = finalAdd;
	}
	
	

}
