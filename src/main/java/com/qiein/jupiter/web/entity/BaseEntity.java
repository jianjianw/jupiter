package com.qiein.jupiter.web.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 实体类基类
 *
 * @author JZL 2018-04-03 17:25
 */
public class BaseEntity implements Serializable {

    private int id;

    public BaseEntity() {
    }

    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}