package com.qiein.jupiter.web.entity.dto;

import java.util.Map;

public class QueryMapDTO {
    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 每次查询多少
     */
    private int pageSize;

    /**
     * 条件
     */
    private Map condition;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map getCondition() {
        return condition;
    }

    public void setCondition(Map condition) {
        this.condition = condition;
    }
}
