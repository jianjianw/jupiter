package com.qiein.jupiter.web.entity.dto;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/11 11:24
 */
public class CitiesAnalysisParamDTO {
    private Integer start;
    private Integer end;
    private String dicCodes;                        //拍摄类型的code字符串 ，多个用逗号拼接
    private String dicType = "common_type";         //字典类型确定为拍摄类型
    private String provinceNames;                   //省份名称，多选用逗号分隔字符串
    private Integer searchClientType;               //查询的客资类型 1所有客资 2 电话客资 3 微信客资 4 qq客资 5 有电话有微信 6 无电话 7.无微信 8 只有电话 9只有微信 10 只有qq
    private Integer companyId;                      //所属公司id

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getDicCodes() {
        return dicCodes;
    }

    public void setDicCodes(String dicCodes) {
        this.dicCodes = dicCodes;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getProvinceNames() {
        return provinceNames;
    }

    public void setProvinceNames(String provinceNames) {
        this.provinceNames = provinceNames;
    }

    public Integer getSearchClientType() {
        return searchClientType;
    }

    public void setSearchClientType(Integer searchClientType) {
        this.searchClientType = searchClientType;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
