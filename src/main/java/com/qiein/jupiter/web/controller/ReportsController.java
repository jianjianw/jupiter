package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ReportsConditionVO;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResultInfo getDsyyGroupReports(@RequestParam("start") Integer start, @RequestParam("end") Integer end) {
        if (NumUtil.isInValid(start) || NumUtil.isInValid(end)) {
            return ResultInfoUtil.error(ExceptionEnum.START_TIME_OR_END_TIME_IS_NULL);
        }
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();

        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
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
    public ResultInfo getDsyyGroupDetailReports(@RequestParam("start") Integer start, @RequestParam("end") Integer end,@RequestParam(value = "groupId",required = false) String groupId) {
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
    public ResultInfo getDsyyGroupSourceReports(@RequestParam("start") Integer start, @RequestParam("end") Integer end,@RequestParam(value = "groupId",required = false) String groupId) {
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
        return ResultInfoUtil.success(reportService.repateKzLog(queryMapDTO, clientLogDTO));
    }

    /**
     * 获取电商推广渠道统计
     */
    @RequestMapping("/get_dstg_channel_reports")
    public ResultInfo getDstgChannelReports(@RequestParam("start") int start, @RequestParam("end") int end) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "dstgchannelreports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }


    /**
     * 转介绍提报统计
     */
    @RequestMapping("/get_zjs_source_reports")
    public ResultInfo getZjsSourceReports(@RequestParam("start") int start, @RequestParam("end") int end) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsentryreports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 转介绍邀约统计
     */
    @RequestMapping("/get_zjs_invite_reports")
    public ResultInfo getZjsInviteReports(@RequestParam("start") int start, @RequestParam("end") int end) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "zjsinvitereports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 转介绍邀约统计
     */
    @RequestMapping("/get_dstg_invalid_reports")
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
    @RequestMapping("/get_mszx_entry_reports")
    public ResultInfo getMszxEntryReports(int start, int end, String shopIds, String sourceIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("shopids", shopIds);
        reqContent.put("sourceids", sourceIds);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "mszxShopReports");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }

    /**
     * 门市入店统计,,人员明细
     */
    @RequestMapping("/get_mszx_entry_detail_reports")
    public ResultInfo getMszxEntryDetailReports(int start, int end, int shopIds, String sourceIds) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("start", start);
        reqContent.put("end", end);
        reqContent.put("shopids", shopIds);
        reqContent.put("sourceids", sourceIds);
        reqContent.put("companyid", currentLoginStaff.getCompanyId());
        //请求juplat接口
        String json = crmBaseApi.doService(reqContent, "mszxShopReportsDetail");
        return ResultInfoUtil.success(JsonFmtUtil.strContentToJsonObj(json).get("analysis"));
    }
}
