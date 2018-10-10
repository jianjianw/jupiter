package com.qiein.jupiter.web.entity.dto;

/**
 * 公司配置
 *
 * @Author: shiTao
 */
public class CompanyConfigDTO {
    /**
     * 自动分配(电商)
     */
    private boolean autoAllotDs;
    /**
     * 自动分配(转介绍)
     */
    private boolean autoAllotZjs;
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

    public boolean isAutoAllotDs() {
        return autoAllotDs;
    }

    public void setAutoAllotDs(boolean autoAllotDs) {
        this.autoAllotDs = autoAllotDs;
    }

    public boolean isAutoAllotZjs() {
        return autoAllotZjs;
    }

    public void setAutoAllotZjs(boolean autoAllotZjs) {
        this.autoAllotZjs = autoAllotZjs;
    }
}
