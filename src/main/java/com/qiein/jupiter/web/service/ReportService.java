package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
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
    List<DstgGoldDataReportsVO> getDstgAdReports(Integer start, Integer end, Integer companyId,String type);

    /**
     * 电商推广咨询信息方式报表
     *
     * @param start
     * @param end
     * @param companyId
     * @param type
     * @param zxStyleCode
     * @return
     */
    List<DstgZxStyleReportsVO> getDstgZxStyleReports(Integer start, Integer end, int companyId,String type,String zxStyleCode);

    /**
     * 获取无效原因客资报表
     *
     * @param companyId
     * @return
     */
    InvalidReasonReportsVO invalidReasonReports(Integer companyId, String sourceIds, String startTime, String endTime, String typeIds);

    /**
     * 获取电商邀约状态统计报表
     *
     * @param start
     * @param end
     * @param companyId
     * @return
     */
    DsyyStatusReportsHeaderVO getDsyyStatusReports(Integer start, Integer end, int companyId,String type,String groupId);


    /**
     * 获取转介绍月底客资报表
     */
    ZjskzOfMonthVO ZjskzOfMonth(Integer companyId, String month, String type, String sourceIds);

    /**
     * 市域报表分析
     *
     * @param citiesAnalysisParamDTO
     * @return
     */
    List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO);

    /**
     * 省域分析报表
     * @param provinceAnalysisParamDTO
     * @return
     */
    List<ProvinceReportsVO2> getProvinceReport(ProvinceAnalysisParamDTO provinceAnalysisParamDTO);

    /**
     * 查询所有客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsSum(Integer start, Integer end, String typeId, String sourceId, int companyId);


    /**
     * 电商邀约详细报表
     *
     * @param start
     * @param end
     * @param groupId
     * @param companyId
     */
    DsyyStatusReportsHeaderVO getDsyyStatusDetailReports(Integer start, Integer end, String groupId, int companyId,String type);

    /**
     * 转介绍月底客资汇总报表详情
     * @param companyId
     * @param sourceId
     * @param month
     * @return
     */
    ZjskzOfMonthMapVO ZjskzOfMonthIn(Integer companyId, String sourceId, String month);

    /**
     * 老客信息汇总报表
     */
    List<OldKzReportsVO> getOldKzReports(Integer companyId, String startTime, String endTime, String kzNameOrPhone);

    /**
     * 查询总客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsAll(Integer start, Integer end, String typeId, String sourceId,
                                                          int companyId);

    /**
     * 查询待定客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsDdNum(Integer start, Integer end, String typeId,
                                                            String sourceId, int companyId);

    /**
     * 查询无效客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsInvalid(Integer start, Integer end, String typeId,
                                                              String sourceId, int companyId);

    /**
     * 查询有客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsvalid(Integer start, Integer end, String typeId,
                                                            String sourceId, int companyId);


    /**
     * 电商推广咨询方式来源详细报表
     *
     * @param start
     * @param end
     * @param zxStyleCode
     * @param companyId
     * @return
     */
    List<DstgZxStyleReportsVO> getDstgZxStyleSourceRerports(Integer start, Integer end, String zxStyleCode,String type, int companyId);

    /**
     * 渠道订单数据统计
     *
     * @param reportsParamVO
     * @return
     */
    List<SourceOrderDataReportsVO> getSourceOrderDataReports(ReportsParamVO reportsParamVO);

    /**
     * 客资各个状态转化统计
     *
     * @param reportsParamVO
     * @return
     */
    List<JSONObject> getClientStatusTranslateForGroup(ReportsParamVO reportsParamVO);

    /**
     * 关键词报表
     * @param startTime
     * @param endTime
     * @param typeIds
     * @param keyWord
     * @param companyId
     * @return
     */
    List<KeyWordReportsVO> getKeyWordReports(String startTime,String endTime,String typeIds,String keyWord,Integer companyId);
}
