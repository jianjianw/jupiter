package com.qiein.jupiter.web.entity.vo;

import java.util.Map;
/**
 * author xiangliang
 */
public class SourceAndStatusReportsVO {
    private Integer srcId;
    private String srcName;
    private String srcImg;
    private Map<String,Integer> map;

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
