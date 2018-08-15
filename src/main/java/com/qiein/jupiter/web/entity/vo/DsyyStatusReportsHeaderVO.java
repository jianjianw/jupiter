package com.qiein.jupiter.web.entity.vo;

import java.util.List;

/**
 * @author: yyx
 * @Date: 2018-8-15
 */
public class DsyyStatusReportsHeaderVO {
    /**
     *  状态报表表头
     * */
    private List<ClientStatusReportsVO> clientStatusReportsVOList;

    private List<DsyyStatusReportsVO> dsyyStatusReportsHeaderVOS;


    public List<ClientStatusReportsVO> getClientStatusReportsVOList() {
        return clientStatusReportsVOList;
    }

    public void setClientStatusReportsVOList(List<ClientStatusReportsVO> clientStatusReportsVOList) {
        this.clientStatusReportsVOList = clientStatusReportsVOList;
    }

    public List<DsyyStatusReportsVO> getDsyyStatusReportsHeaderVOS() {
        return dsyyStatusReportsHeaderVOS;
    }

    public void setDsyyStatusReportsHeaderVOS(List<DsyyStatusReportsVO> dsyyStatusReportsHeaderVOS) {
        this.dsyyStatusReportsHeaderVOS = dsyyStatusReportsHeaderVOS;
    }
}
