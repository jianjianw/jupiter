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
    private String provinceCodes;                   //省份id，多选用逗号分隔字符串
    private Integer searchClientType;               //查询的客资类型
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

    public String getProvinceCodes() {
        return provinceCodes;
    }

    public void setProvinceCodes(String provinceCodes) {
        this.provinceCodes = provinceCodes;
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
