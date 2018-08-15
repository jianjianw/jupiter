package com.qiein.jupiter.web.entity.vo;

public class ReportsParamSrcMonthVO {
	/**
     * 开始时间
     * */
    private Integer start;

    /**
     * 结束时间
     * */
    private Integer end;
    
    /**
     * 拍摄类型
     * */
    private String typeId;
    
    /**
     * 渠道来源
     * */
    private String sourceId;
    
    /**
     * 客资指标
     * */
    private String kzZB;
    
    /**
     * 公司id
     * */
    private Integer companyId;

    
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getKzZB() {
		return kzZB;
	}

	public void setKzZB(String kzZB) {
		this.kzZB = kzZB;
	}
    
}
