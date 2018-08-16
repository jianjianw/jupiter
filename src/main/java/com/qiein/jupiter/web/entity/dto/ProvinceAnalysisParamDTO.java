package com.qiein.jupiter.web.entity.dto;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/15 14:48
 */
public class ProvinceAnalysisParamDTO {
    private Integer start;
    private Integer end;
    private Integer companyId;
    private String dicCodes;                        //拍摄类型的code字符串 ，多个用逗号拼接
    private String dicType = "common_type";         //字典类型确定为拍摄类型
    private String sourceIds;                       //来源
    private String searchType;                      //查询的属性 总客资 allClientCount 客资量 clientCount 有效量 validClientCount 入店量 comeShopClientCount 成交量 successClientCount
    private Integer searchClientType;               //查询的客资类型 1所有客资 2 电话客资 3 微信客资 4 qq客资 5 有电话有微信 6 无电话 7.无微信 8 只有电话 9只有微信 10 只有qq

    public ProvinceAnalysisParamDTO() {
    }

    public ProvinceAnalysisParamDTO(int companyId) {
        this.companyId = companyId;
    }

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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(String sourceIds) {
        this.sourceIds = sourceIds;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Integer getSearchClientType() {
        return searchClientType;
    }

    public void setSearchClientType(Integer searchClientType) {
        this.searchClientType = searchClientType;
    }

    @Override
    public String toString() {
        return "ProvinceAnalysisParamDTO{" +
                "start=" + start +
                ", end=" + end +
                ", companyId=" + companyId +
                ", dicCodes='" + dicCodes + '\'' +
                ", dicType='" + dicType + '\'' +
                ", sourceIds='" + sourceIds + '\'' +
                ", searchType='" + searchType + '\'' +
                ", searchClientType=" + searchClientType +
                '}';
    }
}
