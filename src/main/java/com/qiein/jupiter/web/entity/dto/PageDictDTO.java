package com.qiein.jupiter.web.entity.dto;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.entity.vo.ChannelDictVO;
import com.qiein.jupiter.web.entity.vo.ShopDictVO;
import com.qiein.jupiter.web.entity.vo.SourceDictVO;

import java.util.List;
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
    private Map<String, ChannelDictVO> channelMap;
    /**
     * 来源字典
     */
    private Map<String, SourceDictVO> sourceMap;
    /**
     * 状态字典
     */
    private Map<String, StatusPO> statusMap;
    /**
     * 公共字典
     */
    private Map<String, List<DictionaryPO>> commonMap;
    /**
     * 拍摄地字典
     */
    private Map<String, ShopDictVO> shopMap;

    public Map<String, ShopDictVO> getShopMap() {
        return shopMap;
    }

    public void setShopMap(Map<String, ShopDictVO> shopMap) {
        this.shopMap = shopMap;
    }

    public Map<String, ChannelDictVO> getChannelMap() {
        return channelMap;
    }

    public void setChannelMap(Map<String, ChannelDictVO> channelMap) {
        this.channelMap = channelMap;
    }

    public Map<String, List<DictionaryPO>> getCommonMap() {
        return commonMap;
    }

    public void setCommonMap(Map<String, List<DictionaryPO>> commonMap) {
        this.commonMap = commonMap;
    }

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
