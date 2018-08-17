package com.qiein.jupiter.web.entity.vo;

import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-16
 */
public class DstgYearReportsVO {
    /**
     * sourceid
     * */
    private Integer sourceId;

    /**
     * 来源名称
     * */
    private String sourceName;

    /**
     * 来源图片
     * */
    private String srcImage;

    /**
     * 每月对应的客资个数
     * */
    private Map<String,Integer> mapList;


    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    public Map<String, Integer> getMapList() {
        return mapList;
    }

    public void setMapList(Map<String, Integer> mapList) {
        this.mapList = mapList;
    }
}
