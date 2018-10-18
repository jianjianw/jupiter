package com.qiein.jupiter.web.entity.dto;


import java.util.HashMap;
import java.util.Map;

/**
 * 公司报表配置
 */
public class CompanyReportConfigDTO {


    private Map<String,Boolean> yeSet = new HashMap<>();

    private Map<String,String[]> wxSet = new HashMap<>();

    private Map<String,String[]> dsddSet = new HashMap<>();

    private Map<String,String[]> zjsValidSet = new HashMap<>();

    private Map<String,String[]> zjsddSet = new HashMap<>();

    private Map<String,Boolean> showSet = new HashMap<>();


    public Map<String, Boolean> getYeSet() {
        return yeSet;
    }

    public void setYeSet(Map<String, Boolean> yeSet) {
        this.yeSet = yeSet;
    }

    public Map<String, String[]> getWxSet() {
        return wxSet;
    }

    public void setWxSet(Map<String, String[]> wxSet) {
        this.wxSet = wxSet;
    }

    public Map<String, String[]> getDsddSet() {
        return dsddSet;
    }

    public void setDsddSet(Map<String, String[]> dsddSet) {
        this.dsddSet = dsddSet;
    }

    public Map<String, String[]> getZjsValidSet() {
        return zjsValidSet;
    }

    public void setZjsValidSet(Map<String, String[]> zjsValidSet) {
        this.zjsValidSet = zjsValidSet;
    }

    public Map<String, String[]> getZjsddSet() {
        return zjsddSet;
    }

    public void setZjsddSet(Map<String, String[]> zjsddSet) {
        this.zjsddSet = zjsddSet;
    }

    public Map<String, Boolean> getShowSet() {
        return showSet;
    }

    public void setShowSet(Map<String, Boolean> showSet) {
        this.showSet = showSet;
    }
}
