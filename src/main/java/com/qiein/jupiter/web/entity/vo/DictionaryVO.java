package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.DictionaryPO;

/**
 * 批量编辑字典VO
 * Created by Tt on 2018/5/23 0023.
 */
public class DictionaryVO extends DictionaryPO{
    private String dicNames;
    private String dicCodes;

    public String getDicNames() {
        return dicNames;
    }

    public void setDicNames(String dicNames) {
        this.dicNames = dicNames;
    }

    public String getDicCodes() {
        return dicCodes;
    }

    public void setDicCodes(String dicCodes) {
        this.dicCodes = dicCodes;
    }
}
