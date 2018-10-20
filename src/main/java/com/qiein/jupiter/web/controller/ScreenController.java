package com.qiein.jupiter.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.http.HttpClient;
import com.qiein.jupiter.util.OkHttpUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.vo.ScreenVO;
import com.qiein.jupiter.web.service.ScreenService;

/**
 * 大屏
 * FileName: ReportsController
 *
 * @author: Hjf
 * @Date: 2018-6-30 17:23
 */
@RestController
@RequestMapping("/screen")
public class ScreenController extends BaseController {
    
    @Autowired
    private ScreenService screenService;

    /**
     * 电商网销大屏--Hjf
     */
    @GetMapping("/get_dsyy_datav")
    public ResultInfo getDSTGDatav(@RequestParam("companyId") String companyId) {
    	
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
    
    /**
     * 电商推广大屏--Hjf
     */
    @GetMapping("/get_dstg_datav")
    public ResultInfo getWXDatav(@RequestParam("companyId") String companyId) {
        
    	Map<String, Object> map=new LinkedHashMap<String, Object>();
        if ("1".equals(companyId)) {
        	//今日电商总客资
            List<ScreenVO> dayKZTotal = screenService.getDayKZTotal();
            //今日电商有效客资
            List<ScreenVO> dayValidKZ = screenService.getDayValidKZ();
            //今日电商入店量
            List<ScreenVO> dayComeKZ = screenService.getDayComeKZ();
            //今日电商订单量
            List<ScreenVO> daySuccessKZ = screenService.getDaySuccessKZ();
            //今日有效客资成本
            List<ScreenVO> dayValidKZcost = screenService.getDayValidKZcost();
            //今日各渠道客资有效量
            List<ScreenVO> daySrcValidKZ = screenService.getDaySrcValidKZ();
            //今日各渠道客资量
            List<ScreenVO> daySrcKZ = screenService.getDaySrcKZ();
            //今日各渠道客资有效率
            List<ScreenVO> daySrcKZValideRate = screenService.getDaySrcKZValideRate();
            
            map.put("dayKZTotal", dayKZTotal);
            map.put("dayValidKZ", dayValidKZ);
            map.put("dayComeKZ", dayComeKZ);
            map.put("daySuccessKZ", daySuccessKZ);
            map.put("dayValidKZcost", dayValidKZcost);
            map.put("daySrcValidKZ", daySrcValidKZ);
            map.put("daySrcKZ", daySrcKZ);
            map.put("daySrcKZValideRate", daySrcKZValideRate);
            return ResultInfoUtil.success(map);
        }
      
        return ResultInfoUtil.error(9999, "查询失败");
    }
}
