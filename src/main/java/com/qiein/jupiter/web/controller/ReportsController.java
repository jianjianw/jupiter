package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.vo.DstgYearReportsVO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表
 * FileName: ReportsController
 *
 * @author: yyx
 * @Date: 2018-6-30 17:23
 */
@RestController
@RequestMapping("/reports")
public class ReportsController extends BaseController {
    @Autowired
    private CrmBaseApi crmBaseApi;

    @Autowired
    private ReportService reportService;

    /**
     * 获取电商来源报表
     */
    @RequestMapping("/getDstgSourceReports")
    public ResultInfo getDstgSourceReports(@RequestBody ReportsConditionVO reportsConditionVO) {
        if (null == reportsConditionVO) {
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }
        if (NumUtil.isInValid(reportsConditionVO.getStart()) || NumUtil.isInValid(reportsConditionVO.getEnd())) {
            return ResultInfoUtil.error(ExceptionEnum.START_TIME_OR_END_TIME_IS_NULL);
        }
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        //todo param
        reqContent.put("start", reportsConditionVO.getStart());
        reqContent.put("end", reportsConditionVO.getEnd());
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("typelimit", reportsConditionVO.getTypeLimit());
        reqContent.put("iscreate",reportsConditionVO.getIsCreate());
        if (StringUtil.isNotEmpty(reportsConditionVO.getSourceId())) {
            reqContent.put("sourceid", reportsConditionVO.getSourceId());
        }
        if (NumUtil.isValid(reportsConditionVO.getPhoneLimit())) {
            reqContent.put("phonelimit", reportsConditionVO.getPhoneLimit());
        }
        if (NumUtil.isValid(reportsConditionVO.getSparelimit())) {
            reqContent.put("sparelimit", reportsConditionVO.getSparelimit());
        }
        if (StringUtil.isNotEmpty(reportsConditionVO.getTypeId())) {
            reqContent.put("phototype", reportsConditionVO.getTypeId());
        }
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "dstgSourceReports");

        if (StringUtil.isEmpty(json) || !"100000".equalsIgnoreCase(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("info").getString("code"))) {
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }
        return ResultInfoUtil.success(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("content").getJSONArray("data"));
    }

    /**
     * 电商邀约报表
     */
    @RequestMapping("get_dsyy_group_reports")
    public ResultInfo getDsyyGroupReports(@RequestParam("start") Integer start, @RequestParam("end") Integer end,
                                          @RequestParam(value = "typeId", required = false) String typeId, @RequestParam(value = "groupIds", required = false) String groupIds, @RequestParam(value = "sourceIds", required = false) String sourceIds) {
        if (NumUtil.isInValid(start) || NumUtil.isInValid(end)) {
            return ResultInfoUtil.error(ExceptionEnum.START_TIME_OR_END_TIME_IS_NULL);
        }
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();

        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("typeId", typeId);
        reqContent.put("groupId", groupIds);
        reqContent.put("sourceIds", sourceIds);
        String json = crmBaseApi.doService(reqContent, "dsyyGroupReports");

        if (StringUtil.isEmpty(json) || !"100000".equalsIgnoreCase(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("info").getString("code"))) {
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }
        return ResultInfoUtil.success(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("content").getJSONArray("data"));
    }

    /**
     * 电商邀约客服详细报表
     */
    @RequestMapping("get_dsyy_group_detail_reports")
    public ResultInfo getDsyyGroupDetailReports(@RequestParam("start") Integer start, @RequestParam("end") Integer end, @RequestParam(value = "groupId", required = false) String groupId) {
        if (NumUtil.isInValid(start) || NumUtil.isInValid(end)) {
            return ResultInfoUtil.error(ExceptionEnum.START_TIME_OR_END_TIME_IS_NULL);
        }
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();

        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("groupid", groupId);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        String json = crmBaseApi.doService(reqContent, "dsyyGroupDetailReports");

        if (StringUtil.isEmpty(json) || !"100000".equalsIgnoreCase(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("info").getString("code"))) {
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }
        return ResultInfoUtil.success(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("content").getJSONArray("data"));
    }

    /**
     * 电商邀约来源详细报表
     */
    @RequestMapping("get_dsyy_group_source_reports")
    public ResultInfo getDsyyGroupSourceReports(@RequestParam("start") Integer start, @RequestParam("end") Integer end, @RequestParam(value = "groupId", required = false) String groupId) {
        if (NumUtil.isInValid(start) || NumUtil.isInValid(end)) {
            return ResultInfoUtil.error(ExceptionEnum.START_TIME_OR_END_TIME_IS_NULL);
        }
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();

        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("groupid", groupId);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        String json = crmBaseApi.doService(reqContent, "dsyyGroupSourceReports");

        if (StringUtil.isEmpty(json) || !"100000".equalsIgnoreCase(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("info").getString("code"))) {
            return ResultInfoUtil.error(ExceptionEnum.UNKNOW_ERROR);
        }
        return ResultInfoUtil.success(JSONObject.parseObject(json).getJSONObject("response").getJSONObject("content").getJSONArray("data"));
    }

    /**
     * 修改联系方式日志
     */
    @PostMapping("/edit_client_phone_log")
    public ResultInfo editClientPhoneLog(@RequestBody JSONObject params) {
        QueryMapDTO queryMapDTO = JSONObject.parseObject(params.getJSONObject("queryMapDTO").toJSONString(), QueryMapDTO.class);
        ClientLogDTO clientLogDTO = JSONObject.parseObject(params.getJSONObject("clientLogDTO").toJSONString(), ClientLogDTO.class);
        StaffPO staff = getCurrentLoginStaff();
        clientLogDTO.setCompanyId(staff.getCompanyId());
        return ResultInfoUtil.success(reportService.editClientPhoneLog(queryMapDTO, clientLogDTO));
    }

    /**
     * 微信扫码日志
     */
    @PostMapping("/wechat_scan_code_log")
    public ResultInfo wechatScanCodeLog(@RequestBody JSONObject params) {
        QueryMapDTO queryMapDTO = JSONObject.parseObject(params.getJSONObject("queryMapDTO").toJSONString(), QueryMapDTO.class);
        ClientLogDTO clientLogDTO = JSONObject.parseObject(params.getJSONObject("clientLogDTO").toJSONString(), ClientLogDTO.class);
        StaffPO staff = getCurrentLoginStaff();
        clientLogDTO.setCompanyId(staff.getCompanyId());
        return ResultInfoUtil.success(reportService.wechatScanCodeLog(queryMapDTO, clientLogDTO));
    }

    /**
     * 重复客资
     */
    @PostMapping("/repate_kz_log")
    public ResultInfo repateKzLog(@RequestBody JSONObject params) {
        QueryMapDTO queryMapDTO = JSONObject.parseObject(params.getJSONObject("queryMapDTO").toJSONString(), QueryMapDTO.class);
        ClientLogDTO clientLogDTO = JSONObject.parseObject(params.getJSONObject("clientLogDTO").toJSONString(), ClientLogDTO.class);
        StaffPO staff = getCurrentLoginStaff();
        clientLogDTO.setCompanyId(staff.getCompanyId());
        clientLogDTO.setStaffId(staff.getId());
        return ResultInfoUtil.success(reportService.repateKzLog(queryMapDTO, clientLogDTO));
    }

    /**
     * 获取电商推广渠道统计
     */
    @RequestMapping("/get_dstg_channel_reports")
    public ResultInfo getDstgChannelReports(@RequestParam("start") int start, @RequestParam("end") int end, String channelIds, String typeLimit) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("channelids", channelIds);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("typelimit", typeLimit);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "dstgchannelreports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }


    /**
     * 转介绍来源统计
     */
    @GetMapping("/get_zjs_source_reports")
    public ResultInfo getZjsSourceReports(@RequestParam("start") int start, @RequestParam("end") int end, String sourceIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("sourceids", sourceIds);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsentryreports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 转介绍来源统计-详情
     */
    @GetMapping("/get_zjs_source_reports_detail")
    public ResultInfo getZjsSourceReportsDetail(@RequestParam("start") int start, @RequestParam("end") int end, int sourceId) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("sourceid", sourceId);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsentryreportsDetail");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }


    /**
     * 转介绍邀约统计
     */
    @GetMapping("/get_zjs_invite_reports")
    public ResultInfo getZjsInviteReports(@RequestParam("start") int start, @RequestParam("end") int end, String groupIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("groupids", groupIds);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsinvitereports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 转介绍邀约统计--人员详情
     */
    @GetMapping("/get_zjs_invite_reports_detail")
    public ResultInfo getZjsInviteReportsDetail(@RequestParam("start") int start, @RequestParam("end") int end, String groupIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("groupid", groupIds);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsInviteReportsDetail");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 电商推广无效原因统计
     */
    @GetMapping("/get_dstg_invalid_reports")
    public ResultInfo getDstgInvalidReports(@RequestParam("start") int start, @RequestParam("end") int end) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "dstginvalidreports");

        HashMap<String, Object> result = new HashMap<>();
        result.put("title", JsonFmtUtil.strContentToJsonObj(json).get("title"));
        result.put("analysis", JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
        return ResultInfoUtil.success(result);
    }


    /**
     * 转介绍全员提报统计
     */
    @GetMapping("/get_zjs_staff_entry_reports")
    public ResultInfo getZjsStaffEntryReports(@RequestParam("start") int start, @RequestParam("end") int end, String staffIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("staffids", staffIds);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsEntryStaffReports");

        HashMap<String, Object> result = new HashMap<>();
        result.put("analysis", JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
        return ResultInfoUtil.success(result);
    }

    /**
     * 电商推广-推广客资统计
     */
    @GetMapping("/get_dscj_tg_client_info_reports")
    public ResultInfo getDscjTgClientInfoReports(int start, int end, String typeIds, String sourceIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("sourceids", sourceIds);
        reqContent.put("typeids", typeIds);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "dscjCountReports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 电商推广-推广客资 详情统计
     */
    @GetMapping("/get_dscj_tg_client_info_detail_reports")
    public ResultInfo getDscjTgClientInfoDetailReports(int start, int end, String typeIds, String sourceIds, String groupId) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        reqContent.put("sourceids", sourceIds);
        reqContent.put("typeids", typeIds);
        reqContent.put("groupid", groupId);
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "dscjDetailCountReports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /*
     * 门市入店统计
     */
    @GetMapping("/get_mszx_entry_reports")
    public ResultInfo getMszxEntryReports(int start, int end, String shopIds, String sourceId) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("shopids", shopIds);
        reqContent.put("sourceids", sourceId);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "mszxShopReports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 门市入店统计,,人员明细
     */
    @GetMapping("/get_mszx_entry_detail_reports")
    public ResultInfo getMszxEntryDetailReports(int start, int end, int shopId, @RequestParam(value = "sourceId", required = false) String sourceId)

    {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("shopids", shopId);
        reqContent.put("sourceids", sourceId);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "mszxShopReportsDetail");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }


    /**
     * 获取电商推广广告报表
     */
    @GetMapping("/get_dstg_ad_reports")
    public ResultInfo getDstgAdReports(Integer start, Integer end, @RequestParam(value = "type", required = false) String type, Integer page) {
        if (null == page || 0 == page) {
            page = 1;
        }
        StaffPO staffPO = getCurrentLoginStaff();
        PageInfo dstgAdReports = reportService.getDstgAdReports(start, end, staffPO.getCompanyId(), type, page);
        return ResultInfoUtil.success(dstgAdReports);
    }

    /**
     * 电商推广咨询信息方式报表
     */
    @GetMapping("/get_dstg_zx_style_reports")
    public ResultInfo getDstgZxStyleReports(Integer start, Integer end, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "zxStyleCode", required = false) String zxStyleCode, @RequestParam(value = "sourceIds") String sourceIds, @RequestParam(value = "collectorId", required = false) String collectorId) {
        StaffPO staffPO = getCurrentLoginStaff();
        List<DstgZxStyleReportsVO> dstgGoldDataReportsVO = reportService.getDstgZxStyleReports(start, end, staffPO.getCompanyId(), type, zxStyleCode, sourceIds, collectorId);
        return ResultInfoUtil.success(dstgGoldDataReportsVO);
    }

    /**
     * 电商推广咨询方式来源报表
     */
    @GetMapping("/get_dstg_zx_style_source_reports")
    public ResultInfo getDstgZxStyleSourceReports(Integer start, Integer end, String zxStyleCode, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "collectorId", required = false) String collectorId) {
        StaffPO staffPO = getCurrentLoginStaff();
        List<DstgZxStyleReportsVO> dstgZxStyleReportsVOS = reportService.getDstgZxStyleSourceRerports(start, end, zxStyleCode, type, staffPO.getCompanyId(), collectorId);
        return ResultInfoUtil.success(dstgZxStyleReportsVOS);
    }

    /**
     * 客资各状态转发统计
     */
    @GetMapping("/get_dsyy_status_reports")
    public ResultInfo getDSyyStatusReports(Integer start, Integer end, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "groupId", required = false) String groupId) {
        StaffPO staffPO = getCurrentLoginStaff();
        DsyyStatusReportsHeaderVO dsyyStatusReports = reportService.getDsyyStatusReports(start, end, staffPO.getCompanyId(), type, groupId);
        return ResultInfoUtil.success(dsyyStatusReports);
    }

    /**
     * 电商邀约详细统计
     */
    @GetMapping("/get_dsyy_status_detail_reports")
    public ResultInfo getDsyyStatusReports(Integer start, Integer end, String groupId, @RequestParam(value = "type", required = false) String type) {
        StaffPO staffPO = getCurrentLoginStaff();
        DsyyStatusReportsHeaderVO dsyyStatusReportsHeaderVO = reportService.getDsyyStatusDetailReports(start, end, groupId, staffPO.getCompanyId(), type);
        return ResultInfoUtil.success(dsyyStatusReportsHeaderVO);
    }


    @GetMapping("/receive_ali")
    public ResultInfo receiveAli(String code, String state) {
//        System.out.println(code + "-----------------------------------" + state);
        return ResultInfoUtil.success();
    }

    /**
     * 无效原因报表
     */
    @GetMapping("/invalid_reason_reports")
    public ResultInfo invalidReasonReports(@RequestParam String sourceIds, @RequestParam String startTime, @RequestParam String endTime, @RequestParam String typeIds) {
        return ResultInfoUtil.success(reportService.invalidReasonReports(getCurrentLoginStaff().getCompanyId(), sourceIds, startTime, endTime, typeIds));
    }

    /**
     * 转介绍每月客资报表
     */
    @GetMapping("/zjs_kz_of_month")
    public ResultInfo ZjskzOfMonth(@RequestParam String month, @RequestParam String type, @RequestParam String srcIds, @RequestParam String typeIds) {
        return ResultInfoUtil.success(reportService.ZjskzOfMonth(getCurrentLoginStaff().getCompanyId(), month, typeIds, srcIds, type));
    }

    /**
     * 转介绍每月客资报表内表详情
     */
    @GetMapping("/zjs_kz_of_month_in")
    public ResultInfo ZjskzOfMonthIn(String sourceId, String month) {
        return ResultInfoUtil.success(reportService.ZjskzOfMonthIn(getCurrentLoginStaff().getCompanyId(), sourceId, month));
    }

    /**
     * 电商每月客资报表内表详情
     */
    @GetMapping("/ds_kz_of_month_in")
    public ResultInfo DskzOfMonthIn(String sourceId, String month) {
        return ResultInfoUtil.success(reportService.DskzOfMonthIn(getCurrentLoginStaff().getCompanyId(), sourceId, month));
    }


    /**
     * 获取市域分析报表
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/get_cities_analysis_report")
    public ResultInfo getCitiesAnalysisReport(CitiesAnalysisParamDTO searchKey) {
        if (searchKey.getStart() == null) {
            searchKey.setStart(0);
        }
        if (searchKey.getEnd() == null) {
            searchKey.setEnd(2000000000);
        }
        if (searchKey.getSearchClientType() == null) {
            searchKey.setSearchClientType(1);
        }
        searchKey.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getCityReport(searchKey));
    }

    /**
     * 获取省域分析报表
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/get_province_analysis_report")
    public ResultInfo getProvinceAnalysisReport(ProvinceAnalysisParamDTO searchKey) {
        if (searchKey.getStart() == null) {
            searchKey.setStart(0);
        }
        if (searchKey.getEnd() == null) {
            searchKey.setEnd(2000000000);
        }
        if (searchKey.getSearchClientType() == null) {
            searchKey.setSearchClientType(1);
        }
        searchKey.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getProvinceReport(searchKey));
    }

    /**
     * 电商推广月度客资汇总报表--Hjf
     */
    @GetMapping("/get_dstg_src_month_reports")
    public ResultInfo getDSTGSrcMonthReports(@RequestParam("month") String month, @RequestParam("typeId") String typeId, @RequestParam("sourceId") String sourceId, @RequestParam("kzZB") String kzZB) {
        StaffPO staffPO = getCurrentLoginStaff();
        //查询总客资
        if (StringUtil.isEmpty(kzZB) || "sum".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsSum(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询客资量
        if (StringUtil.isNotEmpty(kzZB) && "all".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsAll(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询待定客资
        if (StringUtil.isNotEmpty(kzZB) && "ddnum".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsDdNum(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }

        //查询无效客资
        if (StringUtil.isNotEmpty(kzZB) && "invalid".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsInvalid(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }

        //查询有效客资
        if (StringUtil.isNotEmpty(kzZB) && "valid".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsvalid(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询入店量
        if (StringUtil.isNotEmpty(kzZB) && "come".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCome(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询成交量
        if (StringUtil.isNotEmpty(kzZB) && "success".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsSuccess(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询有效率
        if (StringUtil.isNotEmpty(kzZB) && "validrate".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsValidRate(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询无效率
        if (StringUtil.isNotEmpty(kzZB) && "invalidrate".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsInValidRate(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询待定率
        if (StringUtil.isNotEmpty(kzZB) && "ddnumrate".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsDdnumRate(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询毛客资入店率
        if (StringUtil.isNotEmpty(kzZB) && "comerate".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsComeRate(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询有效客资入店率
        if (StringUtil.isNotEmpty(kzZB) && "validcomerate".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsValidComeRate(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //查询入店成交率
        if (StringUtil.isNotEmpty(kzZB) && "successrate".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsSuccessRate(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //毛客资成交率
        if (StringUtil.isNotEmpty(kzZB) && "successrate1".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsSuccessRate1(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //有效客资成交率
        if (StringUtil.isNotEmpty(kzZB) && "successrate2".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsSuccessRate2(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //花费
        if (StringUtil.isNotEmpty(kzZB) && "cost".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCost(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //毛客资成本
        if (StringUtil.isNotEmpty(kzZB) && "costkz".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCostKZ(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //有效客资成本
        if (StringUtil.isNotEmpty(kzZB) && "costvalidkz".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCostValidKZ(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //入店成本
        if (StringUtil.isNotEmpty(kzZB) && "costcomekz".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCostComeKZ(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //成交成本
        if (StringUtil.isNotEmpty(kzZB) && "costsuccesskz".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCostSuccessKZ(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        //成交均价
        if (StringUtil.isNotEmpty(kzZB) && "successavg".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsCostSuccessAvg(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        // 营业额
        if (StringUtil.isNotEmpty(kzZB) && "amount".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsAmount(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        // ROI
        if (StringUtil.isNotEmpty(kzZB) && "roi".equals(kzZB)) {
            List<Map<String, Object>> dstgReportsSrcMonthVO = reportService.getDSTGSrcMonthReportsROI(month, typeId, sourceId, staffPO.getCompanyId());
            return ResultInfoUtil.success(dstgReportsSrcMonthVO);
        }
        return ResultInfoUtil.error(9999, "查询失败");
    }

    /**
     * 老客信息汇总报表
     *
     * @param startTime
     * @param endTime
     * @param kzNameOrPhone
     * @return
     */
    @GetMapping("/get_old_kz_reports")
    public ResultInfo getOldKzReports(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String kzNameOrPhone) {
        return ResultInfoUtil.success(reportService.getOldKzReports(getCurrentLoginStaff().getCompanyId(), startTime, endTime, kzNameOrPhone));
    }


    /**
     * 获取渠道订单数据统计
     *
     * @return
     */
    @PostMapping("/get_source_order_data_reports")
    public ResultInfo getSourceOrderDataReports(@RequestBody ReportsParamVO reportsParamVO) {
        reportsParamVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getSourceOrderDataReports(reportsParamVO));
    }


    /**
     * 获取客资各状态转换 小组
     * BY ST 和易大师重复了，暂时不用
     *
     * @return
     */
    @PostMapping("/get_client_status_translate_reports")
    public ResultInfo getClientStatusTranslateReports(@RequestBody ReportsParamVO reportsParamVO) {
        reportsParamVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getClientStatusTranslateForGroup(reportsParamVO));
    }

    /**
     * 关键词报表
     */
    @GetMapping("/get_key_word_reports")
    public ResultInfo getKeyWordReports(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String keyWord, @RequestParam String typeIds) {
        return ResultInfoUtil.success(reportService.getKeyWordReports(startTime, endTime, typeIds, keyWord, getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 财务 月度订单数据统计
     */
    @PostMapping("/get_cw_month_order_count_reports")
    public ResultInfo getCwMonthOrderCountReports(@RequestBody ReportsParamVO reportsParamVO) {
        reportsParamVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getCwMonthOrderCountReports(reportsParamVO));
    }


    /**
     * 电商推广年度报表
     */
    @GetMapping("/get_dstg_years_reports")
    public ResultInfo getDstgYearsReports(String type, String sourceIds, String years, String conditionType) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
//        List<DstgYearReportsVO> dstgYearReportsVOS = reportService.getDstgYearsReports(type,sourceIds,currentLoginStaff.getCompanyId(),years);
        List<DstgYearReportsVO> dstgYearsReports = reportService.getDstgYearsReports(type, sourceIds, currentLoginStaff.getCompanyId(), years, conditionType);
        return ResultInfoUtil.success(dstgYearsReports);
    }

    /**
     * 转介绍年度报表分析
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/get_zjs_year_report")
    public ResultInfo getZjsYearClientReport(ZjsClientYearReportDTO searchKey) {
        if (searchKey.getYear() == 0) {
            searchKey.setYear(Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())));
        }
        searchKey.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getZjsYearReport(searchKey));
    }


    /**
     * 电商推广年度详细报表
     */
    @GetMapping("/get_dstg_year_detail_reports")
    public ResultInfo getDstgYearDetailReports(String years) {
        StaffPO staffPO = getCurrentLoginStaff();
        List<DstgYearDetailReportsProcessVO> dstgYearReportsVOS = reportService.getDstgYearDetailReports(years, staffPO.getCompanyId());
        return ResultInfoUtil.success(dstgYearReportsVOS);
    }

    /**
     * 转介绍年度详情
     *
     * @param searchKey
     * @return
     */
    @GetMapping("/get_zjs_year_detail_report")
    public ResultInfo getZjsYearClientDetailReport(ZjsClientYearReportDTO searchKey) {
        if (searchKey.getYear() == 0) {
            searchKey.setYear(Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date())));
        }
        searchKey.setCompanyId(getCurrentLoginStaff().getCompanyId());

        return ResultInfoUtil.success(reportService.getZjsYearDetailReport(searchKey));
    }

    /**
     * 获取个人简报
     *
     * @param reportParamDTO
     * @return
     */
    @GetMapping("/get_personal_presentation")
    public ResultInfo getPersonalPresentation(ReportParamDTO reportParamDTO) {
        reportParamDTO.setStaffId(getCurrentLoginStaff().getId());
        reportParamDTO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getPersonalPresentation(reportParamDTO));
    }

    /**
     * 推广渠道报表根据 小组id的详情报表
     */
    @GetMapping("/get_dstg_channel_reports_order_by_src")
    public ResultInfo getDstgChannelReportsOrderBySrc(String groupId, String start, String end, String sourceIds, String typeIds) {
        return ResultInfoUtil.success(reportService.getDstgChannelReportsOrderBySrc(groupId, getCurrentLoginStaff().getCompanyId(), start, end, sourceIds, typeIds));
    }

    /**
     * 客资各个渠道各个状态
     */
    @GetMapping("/get_source_and_status_reports")
    public ResultInfo getSourceAndStatusReports(String appointorIds, String collectorIds, String receptorIds, String start, String end, String groupIds, String typeIds, String sourceIds) {
        return ResultInfoUtil.success(reportService.getSourceAndStatusReports(appointorIds, collectorIds, receptorIds, start, end, groupIds, typeIds, sourceIds, getCurrentLoginStaff().getCompanyId()));
    }

    /**
     * 电商推广订单周期统计
     */
    @PostMapping("/get_dstg_order_cycle_count")
    public ResultInfo getDstgOrderCycleCount(@RequestBody QueryVO queryVO) {
        queryVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getDstgOrderCycleCount(queryVO));
    }

    /**
     * 转介绍报表详情，按客服组汇总
     * */
    @GetMapping("/get_zjs_detail_report_by_group")
    public ResultInfo getZjsDetailReportByGroup(ReportParamDTO reportParamDTO){
        reportParamDTO.setStaffId(getCurrentLoginStaff().getId());
        reportParamDTO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        reportService.getZjsDetailReportByGroup(reportParamDTO);

        return null;
    }

    /**
     * 转介绍报表详情，按客服（邀约员）汇总
     * */
    @GetMapping("/get_zjs_detail_report_by_appointor")
    public ResultInfo getZjsDetailReportByAppointor(ReportParamDTO reportParamDTO){
        reportParamDTO.setStaffId(getCurrentLoginStaff().getId());
        reportParamDTO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        reportService.getZjsDetailReportByGroup(reportParamDTO);

        return null;
    }
    /**
     * 销售中心报表
     */
    @PostMapping("/get_safes_center_reports")
    public ResultInfo getSalesCenterReports(ReportsParamVO reportsParamVO){
        reportsParamVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportService.getSalesCenterReports(reportsParamVO));
    }
}
