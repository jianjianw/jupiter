package com.qiein.jupiter.web.entity.vo;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;
/**
 * 金数据客资日志 显示
 * Author xiangliang
 */
public class GoldCustomerShowVO {
    private PageInfo pageInfo;
    private GoldFingerPO goldFingerPO;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public GoldFingerPO getGoldFingerPO() {
        return goldFingerPO;
    }

    public void setGoldFingerPO(GoldFingerPO goldFingerPO) {
        this.goldFingerPO = goldFingerPO;
    }
}
