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
    private Integer typeId;
    
    /**
     * 渠道来源
     * */
    private Integer sourceId;
    
    /**
     * 客资指标
     * */
    private String kzZB;

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

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getKzZB() {
		return kzZB;
	}

	public void setKzZB(String kzZB) {
		this.kzZB = kzZB;
	}
    
}
