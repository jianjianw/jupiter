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

    /**
     * 自动下线
     */
    private boolean autoOffline;

    /**
     * 录入客资或者修改客资后自动关闭窗口
     */
    private boolean autoCloseClientDetailWindow;

    public boolean isAutoCloseClientDetailWindow() {
        return autoCloseClientDetailWindow;
    }

    public void setAutoCloseClientDetailWindow(boolean autoCloseClientDetailWindow) {
        this.autoCloseClientDetailWindow = autoCloseClientDetailWindow;
    }

    public boolean isAutoOffline() {
        return autoOffline;
    }

    public void setAutoOffline(boolean autoOffline) {
        this.autoOffline = autoOffline;
    }

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
