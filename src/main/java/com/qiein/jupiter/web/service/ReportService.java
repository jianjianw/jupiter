package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.vo.*;

import java.util.List;

public interface ReportService {
    /**
     * 修改联系方式日志
     *
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    PageInfo editClientPhoneLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

    /**
     * 微信扫码日志
     *
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    PageInfo wechatScanCodeLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

    /**
     * 重复客资记录
     *
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    PageInfo repateKzLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO);

    /**
     * 获取电商推广报表
     *
     * @param start
     * @param end
     * @param companyId
     * @return
     */
    List<DstgGoldDataReportsVO> getDstgAdReports(Integer start, Integer end, Integer companyId);

    /**
     * 电商推广咨询信息方式报表
     *
     * @param start
     * @param end
     * @param companyId
     * @return
     */
    List<DstgZxStyleReportsVO> getDstgZxStyleReports(Integer start, Integer end, int companyId);

    /**
     * 获取无效原因客资报表
     *
     * @param companyId
     * @return
     */
    InvalidReasonReportsVO invalidReasonReports(Integer companyId, String sourceIds, String startTime, String endTime, String typeIds);

    /**
     * 获取电商邀约状态统计报表
     * @param start
     * @param end
     * @param companyId
     * @return
     * */
    DsyyStatusReportsHeaderVO getDsyyStatusReports(Integer start, Integer end, int companyId);


    /**
     * 获取转介绍月底客资报表
     */
    ZjskzOfMonthVO ZjskzOfMonth(Integer companyId, String month, String type, String sourceIds);

    /**
     * 市域报表分析
     * @param citiesAnalysisParamDTO
     * @return
     */
    List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO);
    /**
     * 转介绍每月客资报表内表详情
     */
    List<ZjskzOfMonthReportsVO> ZjskzOfMonthIn(Integer companyId, String sourceId, String month);

    /**
     * 电商邀约详细报表
     * @param start
     * @param end
     * @param groupId
     * @param companyId
     * */
    DsyyStatusReportsHeaderVO getDsyyStatusDetailReports(Integer start, Integer end,String groupId, int companyId);

}
