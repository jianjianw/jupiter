package com.qiein.jupiter.web.entity.dto;

public class ClientSourceDTO {

    private int oldSrcId;
    private int NewSrcId;
    private String oldSrcName;
    private String NewSrcName;
    private int companyId;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }


    public int getNewSrcId() {
        return NewSrcId;
    }

    public void setNewSrcId(int newSrcId) {
        NewSrcId = newSrcId;
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

    public String getNewSrcName() {
        return NewSrcName;
    }

    public void setNewSrcName(String newSrcName) {
        NewSrcName = newSrcName;
    }
}
