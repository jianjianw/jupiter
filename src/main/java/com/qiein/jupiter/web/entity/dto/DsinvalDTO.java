package com.qiein.jupiter.web.entity.dto;

import java.io.Serializable;

public class DsinvalDTO implements Serializable {
    private static final long serialVersionUID = 7643712184216258888L;
    private int companyId;
    //无效状态
    private String dsInvalidStatus;
    //待跟踪意向等级
    private String dsInvalidLevel;
    //待定是否计入有效
    private boolean ddIsValid;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getDsInvalidStatus() {
        return dsInvalidStatus;
    }

    public void setDsInvalidStatus(String dsInvalidStatus) {
        this.dsInvalidStatus = dsInvalidStatus;
    }

    public String getDsInvalidLevel() {
        return dsInvalidLevel;
    }

    public void setDsInvalidLevel(String dsInvalidLevel) {
        this.dsInvalidLevel = dsInvalidLevel;
    }

    public boolean isDdIsValid() {
        return ddIsValid;
    }

    public void setDdIsValid(boolean ddIsValid) {
        this.ddIsValid = ddIsValid;
    }
}
