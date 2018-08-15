package com.qiein.jupiter.web.entity.vo;

import java.util.Map;
public class ZjskzOfMonthMapVO {
    private Map<String,Object> clientCountMap;
    private Map<String,Object> validClientCountMap;
    private Map<String,Object> comeShopClientCountMap;
    private Map<String,Object> successClientCountMap;
    private Map<String,Object> validRateMap;
    private Map<String,Object> inValidRateMap;
    private Map<String,Object> validClientComeShopRateMap;
    private Map<String,Object> comeShopSuccessRateMap;

    public Map<String, Object> getClientCountMap() {
        return clientCountMap;
    }

    public void setClientCountMap(Map<String, Object> clientCountMap) {
        this.clientCountMap = clientCountMap;
    }

    public Map<String, Object> getValidClientCountMap() {
        return validClientCountMap;
    }

    public void setValidClientCountMap(Map<String, Object> validClientCountMap) {
        this.validClientCountMap = validClientCountMap;
    }

    public Map<String, Object> getComeShopClientCountMap() {
        return comeShopClientCountMap;
    }

    public void setComeShopClientCountMap(Map<String, Object> comeShopClientCountMap) {
        this.comeShopClientCountMap = comeShopClientCountMap;
    }

    public Map<String, Object> getSuccessClientCountMap() {
        return successClientCountMap;
    }

    public void setSuccessClientCountMap(Map<String, Object> successClientCountMap) {
        this.successClientCountMap = successClientCountMap;
    }

    public Map<String, Object> getValidRateMap() {
        return validRateMap;
    }

    public void setValidRateMap(Map<String, Object> validRateMap) {
        this.validRateMap = validRateMap;
    }

    public Map<String, Object> getInValidRateMap() {
        return inValidRateMap;
    }

    public void setInValidRateMap(Map<String, Object> inValidRateMap) {
        this.inValidRateMap = inValidRateMap;
    }

    public Map<String, Object> getValidClientComeShopRateMap() {
        return validClientComeShopRateMap;
    }

    public void setValidClientComeShopRateMap(Map<String, Object> validClientComeShopRateMap) {
        this.validClientComeShopRateMap = validClientComeShopRateMap;
    }

    public Map<String, Object> getComeShopSuccessRateMap() {
        return comeShopSuccessRateMap;
    }

    public void setComeShopSuccessRateMap(Map<String, Object> comeShopSuccessRateMap) {
        this.comeShopSuccessRateMap = comeShopSuccessRateMap;
    }
}
