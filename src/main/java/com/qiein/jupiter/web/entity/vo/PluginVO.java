package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.PluginPO;

/**
 * 插件权限VO
 * @create by Tt(叶华葳) 2018-06-02 11:15
 */
public class PluginVO extends PluginPO{
    //权限id
    private Integer pmsId;

    public Integer getPmsId() {
        return pmsId;
    }

    public void setPmsId(Integer pmsId) {
        this.pmsId = pmsId;
    }
}
