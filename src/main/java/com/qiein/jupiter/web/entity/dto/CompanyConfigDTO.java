package com.qiein.jupiter.web.entity.dto;

/**
 * 公司配置
 *
 * @Author: shiTao
 */
public class CompanyConfigDTO {
    /**
     * 自动分配
     */
    private boolean autoAllot;
    /**
     * 自动关闭自动分配
     */
    private boolean autoCloseAllot;

    public boolean isAutoCloseAllot() {
        return autoCloseAllot;
    }

    public void setAutoCloseAllot(boolean autoCloseAllot) {
        this.autoCloseAllot = autoCloseAllot;
    }

    public boolean isAutoAllot() {
        return autoAllot;
    }

    public void setAutoAllot(boolean autoAllot) {
        this.autoAllot = autoAllot;
    }
}
