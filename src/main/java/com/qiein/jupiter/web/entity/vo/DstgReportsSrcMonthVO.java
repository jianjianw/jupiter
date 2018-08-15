package com.qiein.jupiter.web.entity.vo;

/**
 * @author: Hjf
 * @Date: 2018-8-10
 */
public class DstgReportsSrcMonthVO {
	
	/**
     * 渠道id
     * */
    private String sourceId;
    
    /**
     * 渠道name
     * */
    private String sourceName;
    
    /**
     * 时间
     * */
    private String day;
    
    /**
     * 总客资
     * */
    private int allClientCount;
    
    /**
     * 客资量
     * */
    private int clientCount;
    
    /**
     * 无效量
     * */
    private int inValidClientCount;
    
    /**
     * 待定量
     * */
    private int pendingClientCount;
    
    /**
     * 有效量
     * */
    private int validClientCount;
    
    /**
     * 入店量
     * */
    private int comeShopClientCount;
    
    /**
     * 成交量
     * */
    private int successClientCount;
    
    
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public int getAllClientCount() {
		return allClientCount;
	}

	public void setAllClientCount(int allClientCount) {
		this.allClientCount = allClientCount;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}

	public int getInValidClientCount() {
		return inValidClientCount;
	}

	public void setInValidClientCount(int inValidClientCount) {
		this.inValidClientCount = inValidClientCount;
	}

	public int getPendingClientCount() {
		return pendingClientCount;
	}

	public void setPendingClientCount(int pendingClientCount) {
		this.pendingClientCount = pendingClientCount;
	}

	public int getValidClientCount() {
		return validClientCount;
	}

	public void setValidClientCount(int validClientCount) {
		this.validClientCount = validClientCount;
	}

	public int getComeShopClientCount() {
		return comeShopClientCount;
	}

	public void setComeShopClientCount(int comeShopClientCount) {
		this.comeShopClientCount = comeShopClientCount;
	}

	public int getSuccessClientCount() {
		return successClientCount;
	}

	public void setSuccessClientCount(int successClientCount) {
		this.successClientCount = successClientCount;
	}
    
}

