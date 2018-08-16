package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.dto.SourceClientDataDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 省域报表VO 从数据库读取出来的对象，还要经过转换成ProvinceReportsVO2 前端才能解析
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/15 15:01
 */
public class ProvinceReportsVO {

    private String dataType;        //数据类型  总客资 客资量 有效量 入店量 成交量
    private String provinceName;        //省名
    private List<SourceClientDataDTO> sourceData  = new ArrayList<>();  //来源数据

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<SourceClientDataDTO> getSourceData() {
        return sourceData;
    }

    public void setSourceData(List<SourceClientDataDTO> sourceData) {
        this.sourceData = sourceData;
    }

    @Override
    public String toString() {
        return "ProvinceReportsVO{" +
                "dataType='" + dataType + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", sourceData=" + sourceData +
                '}';
    }
}
