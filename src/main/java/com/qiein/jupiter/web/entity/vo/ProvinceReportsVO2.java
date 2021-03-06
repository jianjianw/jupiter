package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.dto.ProvinceDataDTO;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 省域报表VO2 只有这个对象前端才能解析
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 13:26
 */
public class ProvinceReportsVO2 implements Comparable<ProvinceReportsVO2>{
    private Integer srcId;
    private String srcImg;
    private String srcName;
    private String dataType;    //数据类型 总客资 客资量 有效量 入店量 成交量
    private List<ProvinceDataDTO> provinceDataList;
    private Map<String,String> provinceDataMap;

    public ProvinceReportsVO2() {
        provinceDataMap = new TreeMap<>();
    }

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

    public List<ProvinceDataDTO> getProvinceDataList() {
        return provinceDataList;
    }

    public void setProvinceDataList(List<ProvinceDataDTO> provinceDataList) {
        this.provinceDataList = provinceDataList;
    }

    public Map<String, String> getProvinceDataMap() {
        return provinceDataMap;
    }

    public void setProvinceDataMap(Map<String, String> provinceDataMap) {
        this.provinceDataMap = provinceDataMap;
    }

    @Override
    public String toString() {
        return "ProvinceReportsVO2{" +
                "srcId=" + srcId +
                ", srcImg='" + srcImg + '\'' +
                ", srcName='" + srcName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", provinceDataList=" + provinceDataList +
                ", provinceDataMap=" + provinceDataMap +
                '}';
    }

    @Override
    public int compareTo(ProvinceReportsVO2 o) {
        Integer one = Integer.valueOf(provinceDataMap.get("合计"));
        Integer two = Integer.valueOf(o.getProvinceDataMap().get("合计"));
        return one>two?-1:one.equals(two)?0:1;
    }
}
