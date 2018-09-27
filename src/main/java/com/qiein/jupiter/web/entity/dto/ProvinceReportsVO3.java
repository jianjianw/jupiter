package com.qiein.jupiter.web.entity.dto;

import java.util.Map;

/**
 * 省域报表返回对象
 * @Auther: Tt(yehuawei)
 * @Date: 2018/9/26 18:51
 */
public class ProvinceReportsVO3 {
    private Integer srcId;
    private String srcImg;
    private String srcName;
    private String dataType;
    private Map<String,String> provinceDataMap;

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Map<String, String> getProvinceDataMap() {
        return provinceDataMap;
    }

    public void setProvinceDataMap(Map<String, String> provinceDataMap) {
        this.provinceDataMap = provinceDataMap;
    }
}
