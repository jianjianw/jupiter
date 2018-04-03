package com.qiein.jupiter.web.entity.vo;

import java.io.Serializable;

/**
 * 实体类基类
 * 
 * @author JZL 2018-04-03 17:25
 *
 */
public class BaseObject implements Serializable {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}