package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/14 10:59
 */
public interface ReportsService防冲突 {
    RegionReportsVO getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO);
}
