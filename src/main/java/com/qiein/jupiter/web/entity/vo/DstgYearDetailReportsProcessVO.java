package com.qiein.jupiter.web.entity.vo;

import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-17
 */
public class DstgYearDetailReportsProcessVO {
    /**
     * 名称
     * */
    private String name;

    /**
     * 处理过的数据
     * */
    private Map<String,Object> processData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProcessData() {
        return processData;
    }

    public void setProcessData(Map<String, Object> processData) {
        this.processData = processData;
    }
}
