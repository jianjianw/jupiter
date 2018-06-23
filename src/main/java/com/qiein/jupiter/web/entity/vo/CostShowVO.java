package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CostPO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CostShowVO {
    //id
    private Integer id;
    //来源ID
    private Integer srcId;
    //总花费
    private BigDecimal totalCost;
    //花费详情
    private List<CostPO> costPOS;
    //来源名称
    private String srcName;
    //来源图片
    private String srcImg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<CostPO> getCostPOS() {
        return costPOS;
    }

    public void setCostPOS(List<CostPO> costPOS) {
        this.costPOS = costPOS;
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
