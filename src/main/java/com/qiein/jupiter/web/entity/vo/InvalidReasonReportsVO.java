package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.DictionaryPO;
import java.util.Map;

import java.util.List;

/**
 * author xiangliang
 */
public class InvalidReasonReportsVO {
    /**
     * 无效原因
     */
    private List<DictionaryPO> invalidReasons;
    /**
     * 客资数量
     */
    private List<InvalidReasonReportsShowVO> invalidReasonKz;

    public List<DictionaryPO> getInvalidReasons() {
        return invalidReasons;
    }

    public void setInvalidReasons(List<DictionaryPO> invalidReasons) {
        this.invalidReasons = invalidReasons;
    }

    public List<InvalidReasonReportsShowVO>  getInvalidReasonKz() {
        return invalidReasonKz;
    }

    public void setInvalidReasonKz(List<InvalidReasonReportsShowVO>  invalidReasonKz) {
        this.invalidReasonKz = invalidReasonKz;
    }
}
