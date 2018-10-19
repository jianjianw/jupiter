package com.qiein.jupiter.web.entity.dto;

/**
 * 公司重复设置
 *
 * @Author: shiTao
 */
public class RepeatDTO {

    /**
     * 拍摄类型是否忽略
     */
    private boolean typeRepeat;
    /**
     * 渠道类型是否忽略
     */
    private boolean srcRepeat;
    /**
     * 状态忽略列表
     */
    private String statusIgnore;
    /**
     * 重复时间忽略类型
     */
    private String timeTypeIgnore;
    /**
     * 忽略日期限制
     */
    private int dayIgnore;

    public boolean getTypeRepeat() {
        return typeRepeat;
    }

    public void setTypeRepeat(boolean typeRepeat) {
        this.typeRepeat = typeRepeat;
    }

    public boolean getSrcRepeat() {
        return srcRepeat;
    }

    public void setSrcRepeat(boolean srcRepeat) {
        this.srcRepeat = srcRepeat;
    }

    public String getStatusIgnore() {
        return statusIgnore;
    }

    public void setStatusIgnore(String statusIgnore) {
        this.statusIgnore = statusIgnore;
    }

    public String getTimeTypeIgnore() {
        return timeTypeIgnore;
    }

    public void setTimeTypeIgnore(String timeTypeIgnore) {
        this.timeTypeIgnore = timeTypeIgnore;
    }

    public int getDayIgnore() {
        return dayIgnore;
    }

    public void setDayIgnore(int dayIgnore) {
        this.dayIgnore = dayIgnore;
    }
}
