package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.vo.DstgGoldDataReportsVO;
import com.qiein.jupiter.web.entity.vo.DstgZxStyleReportsVO;

import java.util.List;

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

    /**
     * 获取电商推广报表
     * @param start
     * @param end
     * @param companyId
     * @return
     * */
    List<DstgGoldDataReportsVO> getDstgAdReports(Integer start, Integer end, Integer companyId);

    /**
     * 电商推广咨询信息方式报表
     * @param start
     * @param end
     * @param companyId
     * @return
     * */
    List<DstgZxStyleReportsVO> getDstgZxStyleReports(Integer start, Integer end, int companyId);
}
