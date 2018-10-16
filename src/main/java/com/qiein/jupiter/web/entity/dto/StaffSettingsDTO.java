package com.qiein.jupiter.web.entity.dto;

/**
 * 员工通用设置
 *
 * @Author: shiTao
 */
public class StaffSettingsDTO {
    /**
     * 自动关闭窗口
     */
    private boolean autoCloseWindow;

    public boolean isAutoCloseWindow() {
        return autoCloseWindow;
    }

    public void setAutoCloseWindow(boolean autoCloseWindow) {
        this.autoCloseWindow = autoCloseWindow;
    }
}
