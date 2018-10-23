package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.vo.DstgYearReportsVO;
import com.qiein.jupiter.web.entity.vo.*;

import java.util.List;
import java.util.Map;

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
    PageInfo getDstgAdReports(Integer start, Integer end, Integer companyId, String type,Integer page);

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
    List<DstgZxStyleReportsVO> getDstgZxStyleReports(Integer start, Integer end, int companyId, String type, String zxStyleCode,String sourceIds,String collectorId);

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
    DsyyStatusReportsHeaderVO getDsyyStatusReports(Integer start, Integer end, int companyId, String type, String groupId);


    /**
     * 获取转介绍月底客资报表
     */
    List<ZjsKzOfMonthShowVO> ZjskzOfMonth(Integer companyId, String month, String typeIds, String sourceIds,String type);

    /**
     * 市域报表分析
     *
     * @param citiesAnalysisParamDTO
     * @return
     */
    List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO);

    /**
     * 省域分析报表
     *
     * @param provinceAnalysisParamDTO
     * @return
     */
    Map<String,Object> getProvinceReport(ProvinceAnalysisParamDTO provinceAnalysisParamDTO);

    /**
     * 查询所有客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<Map<String, Object>> getDSTGSrcMonthReportsSum(String month, String typeId, String sourceId, int companyId);


    /**
     * 电商邀约详细报表
     *
     * @param start
     * @param end
     * @param groupId
     * @param companyId
     */
    DsyyStatusReportsHeaderVO getDsyyStatusDetailReports(Integer start, Integer end, String groupId, int companyId, String type);

    /**
     * 转介绍月底客资汇总报表详情
     *
     * @param companyId
     * @param sourceId
     * @param month
     * @return
     */
    ZjskzOfMonthMapVO ZjskzOfMonthIn(Integer companyId, String sourceId, String month);
    /**
     * 转介绍月底客资汇总报表详情
     * @param companyId
     * @param sourceId
     * @param month
     * @return
     */
    ZjskzOfMonthMapVO DskzOfMonthIn(Integer companyId, String sourceId, String month);

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
    List<Map<String, Object>> getDSTGSrcMonthReportsAll(String month, String typeId, String sourceId,
                                                          int companyId);

    /**
     * 查询待定客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<Map<String, Object>> getDSTGSrcMonthReportsDdNum(String month, String typeId,
                                                            String sourceId, int companyId);

    /**
     * 查询无效客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<Map<String, Object>> getDSTGSrcMonthReportsInvalid(String month, String typeId,
                                                              String sourceId, int companyId);

    /**
     * 查询有客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    List<Map<String, Object>> getDSTGSrcMonthReportsvalid(String month, String typeId,
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
    List<DstgZxStyleReportsVO> getDstgZxStyleSourceRerports(Integer start, Integer end, String zxStyleCode, String type, int companyId,String collectorId);

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
     *
     * @param startTime
     * @param endTime
     * @param typeIds
     * @param keyWord
     * @param companyId
     * @return
     */
    List<KeyWordReportsVO> getKeyWordReports(String startTime,String endTime,String typeIds,String keyWord,Integer companyId);

    /**
     * 年度报表
     * @param companyId
     * @return
     * */
    List<DstgYearReportsVO> getDstgYearsReports(String type,String typeIds, int companyId,String years,String conditionType);

    /**
     * 财务 月度订单数据统计
     *
     * @param reportsParamVO
     * @return
     */
    JSONObject getCwMonthOrderCountReports(ReportsParamVO reportsParamVO);

    /**
     * 获取转介绍年度分析报表
     * @param zjsClientYearReportDTO
     * @return
     */
    List<ZjsClientYearReportVO2> getZjsYearReport(ZjsClientYearReportDTO zjsClientYearReportDTO);

    /**
     * 获取电商推广年度详情报表
     * */
    List<DstgYearDetailReportsProcessVO> getDstgYearDetailReports(String years, Integer companyId);

    /**
     * 转介绍年度详情报表
     * @param zjsClientYearReportDTO
     * @return
     */
    List<Map<String, Object>> getZjsYearDetailReport(ZjsClientYearReportDTO zjsClientYearReportDTO);

    /**
     * 获取员工个人简报
     * @param reportParamDTO
     * @return
     */
    Map<String,RegionReportsVO> getPersonalPresentation(ReportParamDTO reportParamDTO);

    /**
     * 推广渠道报表根据 小组id的详情报表
     * @param groupId
     * @param companyId
     * @return
     */
    List<DstgChannelReportsOrderBySrcVO> getDstgChannelReportsOrderBySrc(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds);
    /**
    * 客资各个渠道各个状态
     */
    SourceAndStatusReportsShowVO getSourceAndStatusReports(String appointorIds,String collectorIds,String receptorIds,String start,String end,String groupIds,String typeIds,String sourceIds,Integer companyId);
    
    /**
     * 电商推广月度报表入店量量--HJF
     * @param reportParamDTO
     */
    List<Map<String, Object>> getDSTGSrcMonthReportsCome(String month, String typeId, String sourceId, int companyId);
	/**
     * 电商推广月度报表成交量--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsSuccess(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度报表有效客资量--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsValidRate(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度报表无效客资量--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsInValidRate(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度报表待定客资量--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsDdnumRate(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度报表毛客资待定率--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsComeRate(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度报表有效客资入店率--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsValidComeRate(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度报表入店成交率--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsSuccessRate(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度毛客资成交率--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsSuccessRate1(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度有效客资成交率--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsSuccessRate2(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度花费--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsCost(String month, String typeId, String sourceId, int companyId);
	/**
     * 电商推广月度毛客资成本--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsCostKZ(String month, String typeId, String sourceId, int companyId);
	/**
     * 电商推广月度有效客资成本--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsCostValidKZ(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度入店成本--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsCostComeKZ(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度成交成本--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsCostSuccessKZ(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度成交均价--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsCostSuccessAvg(String month, String typeId, String sourceId,
			int companyId);
	/**
     * 电商推广月度营业额--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsAmount(String month, String typeId, String sourceId, int companyId);
	/**
     * 电商推广月度ROI--HJF
     * @param reportParamDTO
     */
	List<Map<String, Object>> getDSTGSrcMonthReportsROI(String month, String typeId, String sourceId, int companyId);

    /**
     * 转介绍报表，按客服组汇总
     * */
    public List<Object> getZjsGroupReport(ReportsParamVO reportsParamVO);

    /**
     * 电商推广订单周期统计
     */
    JSONObject getDstgOrderCycleCount(QueryVO queryVO);
    /**
     * 销售中心报表
     */
    List<SalesCenterReportsVO> getSalesCenterReports(ReportsParamVO reportsParamVO);

    /**
     * 转介绍报表详情，按客服组内的员工汇总
     * */
    public List<Object> getZjsGroupDetailReport(ReportsParamVO reportsParamVO);
    /**
     * 专业中心报表
     */
    List<ProfessionalCenterVO> getProfessionalCenterVO(ReportsParamVO reportsParamVO);

	List<Map<String, Object>> getDSTGSrcMonthReportsAppointment(String month, String typeId, String sourceId,
			int companyId);
}
