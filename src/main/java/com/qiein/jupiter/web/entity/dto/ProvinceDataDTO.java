package com.qiein.jupiter.web.entity.dto;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/16 13:47
 */
public class ProvinceDataDTO {
    private String provinceName;    //省名
    private String dataNum;            //数据值

    public ProvinceDataDTO() {
    }

    public ProvinceDataDTO(String provinceName, String dataNum) {
        this.provinceName = provinceName;
        this.dataNum = dataNum;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    @Override
    public String toString() {
        return "ProvinceDataDTO{" +
                "provinceName='" + provinceName + '\'' +
                ", dataNum=" + dataNum +
                '}';
    }
}
