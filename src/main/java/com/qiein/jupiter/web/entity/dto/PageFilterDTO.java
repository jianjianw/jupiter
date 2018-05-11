package com.qiein.jupiter.web.entity.dto;

/**
 * 页面过滤DTO
 */
public class PageFilterDTO {
    /**
     * 显示的文字
     */
    private String label;
    /**
     * 值
     */
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
