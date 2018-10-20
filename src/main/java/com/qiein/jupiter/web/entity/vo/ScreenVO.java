package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.BaseEntity;

import java.util.List;

/**
 * 大屏
 */
public class ScreenVO extends BaseEntity {
	
    private static final long serialVersionUID = 1L;
    
    private String name;
    
    /**
     * 合计
     */
    private int total;
    
    /**
     * 单个合计
     */
    private int value;
    /**
     * 有效率
     */
    private double rate;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
