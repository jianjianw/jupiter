package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/11 11:16
 */
public class ReportsController防止多人提交git冲突 extends BaseController{

    @GetMapping("/get_cities_analysis_report")
    public ResultInfo getCitiesAnalysisReport(CitiesAnalysisParamDTO searchKey){
        StaffPO staffPO = getCurrentLoginStaff();

        return ResultInfoUtil.success(new RegionReportsVO());
    }
}
