package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.service.ReportsService防冲突;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/11 11:16
 */
@RestController
@RequestMapping("/city")
public class ReportsController防止多人提交git冲突 extends BaseController{

    @Autowired
    private ReportsService防冲突 reportsService ;

    @GetMapping("/get_cities_analysis_report")
    public ResultInfo getCitiesAnalysisReport(CitiesAnalysisParamDTO searchKey){
        //TODO 给默认时间
        if (searchKey.getStart() == null){
            searchKey.setStart(0);
        }
        if (searchKey.getEnd() == null){
            searchKey.setEnd(2000000000);
        }
        if (searchKey.getSearchClientType() == null){
            searchKey.setSearchClientType(1);
        }
        searchKey.setCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(reportsService.getCityReport(searchKey));
    }
}
