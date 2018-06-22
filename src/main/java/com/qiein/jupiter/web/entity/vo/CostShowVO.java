package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CostPO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CostShowVO {
    //来源ID
    private Integer srcId;
    //总花费
    private BigDecimal totalCost;
    //花费详情
    private List<CostPO> costPOS;

    private Map<String,Integer> map;

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
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
}
