package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;

public interface ReportService {
    /**
     * 修改联系方式日志
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    PageInfo editClientPhoneLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

    /**
     * 微信扫码日志
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    PageInfo wechatScanCodeLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

    /**
     * 重复客资记录
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    PageInfo repateKzLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);
}
