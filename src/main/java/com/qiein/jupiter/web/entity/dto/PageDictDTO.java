package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.entity.vo.SourceDictVO;

import java.util.Map;

/**
 * 页面状态字典
 *
 * @Author: shiTao
 */
public class PageDictDTO {

    /**
     * 渠道字典
     */
    private Map<String, SourceDictVO> sourceMap;

    /**
     * 状态字典
     */
    private Map<String, StatusPO> statusMap;

    public Map<String, SourceDictVO> getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map<String, SourceDictVO> sourceMap) {
        this.sourceMap = sourceMap;
    }

    public Map<String, StatusPO> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, StatusPO> statusMap) {
        this.statusMap = statusMap;
    }
}