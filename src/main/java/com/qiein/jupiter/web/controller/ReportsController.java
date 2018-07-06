package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.ClientLogDao;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ReportsConditionVO;
import com.qiein.jupiter.web.service.ClientEditService;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

/**
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


}
