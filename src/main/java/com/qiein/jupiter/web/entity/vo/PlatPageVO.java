package com.qiein.jupiter.web.entity.vo;

import java.util.List;

/**
 * 平台分页
 *
 * @Author: shiTao
 */
public class PlatPageVO {
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页多少条
     */
    private int pageSize;
    /**
     * 总数统计
     */
    private int totalCount;
    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 数据
     */
    private List data;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
