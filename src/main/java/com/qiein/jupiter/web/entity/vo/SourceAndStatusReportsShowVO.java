package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.StatusPO;

import java.util.List;

/**
 * author xiangliang
 */
public class SourceAndStatusReportsShowVO {
    private List<SourceAndStatusReportsVO> list;
    private List<StatusPO> statusPO;

    public List<SourceAndStatusReportsVO> getList() {
        return list;
    }

    public void setList(List<SourceAndStatusReportsVO> list) {
        this.list = list;
    }

    public List<StatusPO> getStatusPO() {
        return statusPO;
    }

    public void setStatusPO(List<StatusPO> statusPO) {
        this.statusPO = statusPO;
    }
}
