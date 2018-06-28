package com.qiein.jupiter.web.entity.dto;

public class DsinvalDTO {
    private Integer companyId;
    //无效状态
    private String dsinvalIdStatus;
    //待跟踪意向等级
    private String dsinvalIdLevel;
    //统计时待定客资是否为有效客资
    private boolean ddsinvalId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getDsinvalIdStatus() {
        return dsinvalIdStatus;
    }

    public void setDsinvalIdStatus(String dsinvalIdStatus) {
        this.dsinvalIdStatus = dsinvalIdStatus;
    }

    public String getDsinvalIdLevel() {
        return dsinvalIdLevel;
    }

    public void setDsinvalIdLevel(String dsinvalIdLevel) {
        this.dsinvalIdLevel = dsinvalIdLevel;
    }

    public boolean isDdsinvalId() {
        return ddsinvalId;
    }

    public void setDdsinvalId(boolean ddsinvalId) {
        this.ddsinvalId = ddsinvalId;
    }
}
