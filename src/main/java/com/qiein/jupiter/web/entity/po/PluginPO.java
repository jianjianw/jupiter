package com.qiein.jupiter.web.entity.po;

import com.qiein.jupiter.web.entity.BaseEntity;

/**
 * Created by Tt
 * on 2018/5/26 0026.
 */
public class PluginPO extends BaseEntity{
    //插件名称
    private String pluginName;
    //插件图标地址
    private String pulginImg;
    //跳转地址
    private String action;
    //是否启用
    private Boolean showFlag;
    //是否为共有插件
    private Boolean publicFlag;
    //是否新页面加载
    private Boolean blankFlag;

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPulginImg() {
        return pulginImg;
    }

    public void setPulginImg(String pulginImg) {
        this.pulginImg = pulginImg;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }

    public Boolean getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(Boolean publicFlag) {
        this.publicFlag = publicFlag;
    }

    public Boolean getBlankFlag() {
        return blankFlag;
    }

    public void setBlankFlag(Boolean blankFlag) {
        this.blankFlag = blankFlag;
    }
}
