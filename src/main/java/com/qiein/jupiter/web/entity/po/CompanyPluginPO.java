package com.qiein.jupiter.web.entity.po;

/**
 * 公司插件PO
 * Created by Tt
 * on 2018/5/26 0026.
 */
public class CompanyPluginPO {
    private Integer pluginId;
    private Integer companyId;
    private Boolean showFlag;

    public Integer getPluginId() {
        return pluginId;
    }

    public void setPluginId(Integer pluginId) {
        this.pluginId = pluginId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }
}
