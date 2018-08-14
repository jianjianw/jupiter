package com.qiein.jupiter.web.entity.vo;


import java.util.List;
import java.util.Map;

/**
 * 转介绍月底客资报表显示数据
 * author xiangliang
 */
public class ZjskzOfMonthVO {
    //表头
    private List<Map<String, Object>> headList;
    //数据
    private List<Map<String, Object>> list;

    public List<Map<String, Object>> getHeadList() {
        return headList;
    }

    public void setHeadList(List<Map<String, Object>> headList) {
        this.headList = headList;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
