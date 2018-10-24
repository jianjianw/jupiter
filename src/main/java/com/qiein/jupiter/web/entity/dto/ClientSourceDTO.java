package com.qiein.jupiter.web.entity.dto;

public class ClientSourceDTO {

    private int OldSrcId;
    private int NewSrcId;
    private String OldSrcName;
    private String NewSrcName;
    private int companyId;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getOldSrcId() {
        return OldSrcId;
    }

    public void setOldSrcId(int oldSrcId) {
        OldSrcId = oldSrcId;
    }

    public int getNewSrcId() {
        return NewSrcId;
    }

    public void setNewSrcId(int newSrcId) {
        NewSrcId = newSrcId;
    }

    public String getOldSrcName() {
        return OldSrcName;
    }

    public void setOldSrcName(String oldSrcName) {
        OldSrcName = oldSrcName;
    }

    public String getNewSrcName() {
        return NewSrcName;
    }

    public void setNewSrcName(String newSrcName) {
        NewSrcName = newSrcName;
    }
}
