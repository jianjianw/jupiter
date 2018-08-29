package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CostPO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CostShowVO {
    //来源ID
    private Integer srcId;
    //实际花费
    private Map<String,BigDecimal> beforeCostMap;
    //花费
    private Map<String,BigDecimal> costMap;
    //来源名称
    private String srcName;
    //来源图片
    private String srcImg;

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public Map<String, BigDecimal> getBeforeCostMap() {
        return beforeCostMap;
    }

    public void setBeforeCostMap(Map<String, BigDecimal> beforeCostMap) {
        this.beforeCostMap = beforeCostMap;
    }

    public Map<String, BigDecimal> getCostMap() {
        return costMap;
    }

    public void setCostMap(Map<String, BigDecimal> costMap) {
        this.costMap = costMap;
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
}
