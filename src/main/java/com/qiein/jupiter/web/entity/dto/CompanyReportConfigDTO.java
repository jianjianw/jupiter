package com.qiein.jupiter.web.entity.dto;


import java.util.HashMap;
import java.util.Map;

/**
 * 公司报表配置
 */
public class CompanyReportConfigDTO {


    private Map<String,Boolean> yeSet = new HashMap<>();

    private Map<String,Integer[]> wxSet = new HashMap<>();

    private Map<String,Integer[]> dsddSet = new HashMap<>();

    private Map<String,Integer[]> zjsValidSet = new HashMap<>();

    private Map<String,Integer[]> zjsddSet = new HashMap<>();

    private Map<String,String> showSet = new HashMap<>();


    public Map<String, Boolean> getYeSet() {
        return yeSet;
    }

    public void setYeSet(Map<String, Boolean> yeSet) {
        this.yeSet = yeSet;
    }

    public Map<String, Integer[]> getWxSet() {
        return wxSet;
    }

    public void setWxSet(Map<String, Integer[]> wxSet) {
        this.wxSet = wxSet;
    }

    public Map<String, Integer[]> getDsddSet() {
        return dsddSet;
    }

    public void setDsddSet(Map<String, Integer[]> dsddSet) {
        this.dsddSet = dsddSet;
    }

    public Map<String, Integer[]> getZjsValidSet() {
        return zjsValidSet;
    }

    public void setZjsValidSet(Map<String, Integer[]> zjsValidSet) {
        this.zjsValidSet = zjsValidSet;
    }

    public Map<String, Integer[]> getZjsddSet() {
        return zjsddSet;
    }

    public void setZjsddSet(Map<String, Integer[]> zjsddSet) {
        this.zjsddSet = zjsddSet;
    }

    public Map<String, String> getShowSet() {
        return showSet;
    }

    public void setShowSet(Map<String, String> showSet) {
        this.showSet = showSet;
    }
}
