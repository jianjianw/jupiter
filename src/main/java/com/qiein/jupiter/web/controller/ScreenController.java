package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.ReportService;
import com.qiein.jupiter.web.service.ScreenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
@RequestMapping("/screen")
public class ScreenController extends BaseController {
    
    @Autowired
    private ScreenService screenService;

    /**
     * 电商推广月度客资汇总报表--Hjf
     */
    @GetMapping("/get_dstg_datav")
    public ResultInfo getDSTGDatav(@RequestParam("companyId") String companyId) {
        //StaffPO staffPO = getCurrentLoginStaff();
        //查询总客资
    	Map<String, Object> map=new LinkedHashMap<String, Object>();
        if ("1".equals(companyId)) {
        	//今日电商总客资
            List<ScreenVO> dayKZTotal = screenService.getDayKZTotal();
            //电商本月总客资
            List<ScreenVO> monthKZTotal=screenService.getMonthKZTotal();
            //网销今日待联系新客资
            List<ScreenVO> ddNum=screenService.getDdNum();
            //网销今日在线数
            List<ScreenVO> wXFlag=screenService.getWXFlag();
            //今日网销各组客资量
            List<ScreenVO> wXGroupKzNum=screenService.getWXKzNum();
            //今日网销各组进店量
            List<ScreenVO> wXGroupKzComeNum=screenService.getWXKzComeNum();
            //本月网销各组客资量
            List<ScreenVO> wXGroupKzNumMonth=screenService.getWXGroupKzNumMonth();
            //本周网销各组进店量
            List<ScreenVO> wXGroupKzComeNumWeek=screenService.WXGroupKzComeNumWeek();
            //本月网销各组进店量
            List<ScreenVO> wXGroupKzComeNumMonth=screenService.WXGroupKzComeNumMonth();
            
            map.put("dayKZTotal", dayKZTotal);
            map.put("monthKZTotal", monthKZTotal);
            map.put("DdNum", ddNum);
            map.put("WXFlag", wXFlag);
            map.put("wXGroupKzNum", wXGroupKzNum);
            map.put("wXGroupKzComeNum", wXGroupKzComeNum);
            map.put("wXGroupKzNumMonth", wXGroupKzNumMonth);
            map.put("wXGroupKzComeNumWeek", wXGroupKzComeNumWeek);
            map.put("wXGroupKzComeNumMonth", wXGroupKzComeNumMonth);
            return ResultInfoUtil.success(map);
        }
      
        return ResultInfoUtil.error(9999, "查询失败");
    }

}
