package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.GoldFingerPO;

import java.util.List;

public class GoldFingerShowVO {
    //金数据表单的列表
    private List<GoldFingerPO> list;
    //统一postURL
    private String postUrl;

    public List<GoldFingerPO> getList() {
        return list;
    }

    public void setList(List<GoldFingerPO> list) {
        this.list = list;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
