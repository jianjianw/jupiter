package com.qiein.jupiter.web.entity.po;

public class CostLogPO {
    private Integer id;//日志id
    private Integer costId;//花费id
    private Integer operaId;// 操作者ID
    private String operaName;// 操作者姓名
    private String memo;// 操作描述
    private String operaTime;// 操作时间
    private String operaIp;// 操作IP
    private Integer companyId;// 所属企业ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }

    public Integer getOperaId() {
        return operaId;
    }

    public void setOperaId(Integer operaId) {
        this.operaId = operaId;
    }

    public String getOperaName() {
        return operaName;
    }

    public void setOperaName(String operaName) {
        this.operaName = operaName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperaTime() {
        return operaTime;
    }

    public void setOperaTime(String operaTime) {
        this.operaTime = operaTime;
    }

    public String getOperaIp() {
        return operaIp;
    }

    public void setOperaIp(String operaIp) {
        this.operaIp = operaIp;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
