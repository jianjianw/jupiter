package com.qiein.jupiter.web.entity.dto;

public class ClientSourceDTO {

    private int oldSrcId;
    private int newSrcId;
    private String oldSrcName;
    private String newSrcName;
    private int companyId;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getNewSrcId() {
        return newSrcId;
    }

    public void setNewSrcId(int newSrcId) {
        this.newSrcId = newSrcId;
    }

    public String getNewSrcName() {
        return newSrcName;
    }

    public void setNewSrcName(String newSrcName) {
        this.newSrcName = newSrcName;
    }

    public int getOldSrcId() {
        return oldSrcId;
    }

    public void setOldSrcId(int oldSrcId) {
        this.oldSrcId = oldSrcId;
    }

    public String getOldSrcName() {
        return oldSrcName;
    }

    public void setOldSrcName(String oldSrcName) {
        this.oldSrcName = oldSrcName;
    }


}
