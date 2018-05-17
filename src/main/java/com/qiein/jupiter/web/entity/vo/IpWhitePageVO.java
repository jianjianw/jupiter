package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.IpWhitePO;

import java.util.List;

/**
 * 安全IP页面
 *
 * @Author: shiTao
 */
public class IpWhitePageVO {
    /**
     * 企业是否开启IP限制
     */
    private int limitFlag;
    /**
     * 企业安全IP名单
     */
    private List<IpWhitePO> ipWhiteList;

    public int getLimitFlag() {
        return limitFlag;
    }

    public void setLimitFlag(int limitFlag) {
        this.limitFlag = limitFlag;
    }

    public List<IpWhitePO> getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(List<IpWhitePO> ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }
}
