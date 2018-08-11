package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
import java.util.Map;

import java.util.List;

public class InvalidReasonReportsVO {
    /**
     * 无效原因
     */
    private List<DictionaryPO> invalidReasons;
    /**
     * 客资数量
     */
    private List<Map<String,Object>> invalidReasonKz;

    public List<DictionaryPO> getInvalidReasons() {
        return invalidReasons;
    }

    public void setInvalidReasons(List<DictionaryPO> invalidReasons) {
        this.invalidReasons = invalidReasons;
    }

    public List<Map<String,Object>>  getInvalidReasonKz() {
        return invalidReasonKz;
    }

    public void setInvalidReasonKz(List<Map<String,Object>>  invalidReasonKz) {
        this.invalidReasonKz = invalidReasonKz;
    }
}
