package com.qiein.jupiter.web.entity.po;

import java.io.Serializable;
import java.util.Date;
/**
 * ip白名单
 * 
 * @author XiangLiang 2018/5/16 14:35
 *
 */
public class IpWhitePO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1708260997998320026L;
	/**
     * ip id
     */
	private int id;
	/**
     * 能使用的ip地址
     */
	private String ip;
	/**
     * 创建者的id
     */
	private int creatoerId;
	/**
     * 创建者 的名字
     */
	private String creatoerName;
	/**
     * 创建的时间
     */
	private Date createTime;
	/**
     * 备注
     */
	private String memo;
	/**
     * 企业id
     */
	private int companyId;
	/**
     * 企业名称
     */
	private String companyName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getCreatoerId() {
		return creatoerId;
	}
	public void setCreatoerId(int creatoerId) {
		this.creatoerId = creatoerId;
	}
	public String getCreatoerName() {
		return creatoerName;
	}
	public void setCreatoerName(String creatoerName) {
		this.creatoerName = creatoerName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
}
