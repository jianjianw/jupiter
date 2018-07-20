package com.qiein.jupiter.web.entity.vo;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.po.GoldFingerPO;

import java.util.List;

public class GoldFingerShowVO {
    //金数据表单的列表
    private PageInfo
            <GoldFingerPO> list;
    //统一postURL
    private String postUrl;

    public PageInfo<GoldFingerPO> getList() {
        return list;
    }

    public void setList(PageInfo<GoldFingerPO> list) {
        this.list = list;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
