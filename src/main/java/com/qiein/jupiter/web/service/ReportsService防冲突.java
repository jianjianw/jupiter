package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ZjsClientYearReportDTO;
import com.qiein.jupiter.web.entity.vo.ProvinceReportsVO;
import com.qiein.jupiter.web.entity.vo.ProvinceReportsVO2;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;
import com.qiein.jupiter.web.entity.vo.ZjsClientYearReportVO2;

import java.util.List;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/14 10:59
 */
public interface ReportsService防冲突 {
    List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO);

    List<ProvinceReportsVO2> getProvinceReport(ProvinceAnalysisParamDTO provinceAnalysisParamDTO);

    List<ZjsClientYearReportVO2> getZjsYearReport(ZjsClientYearReportDTO zjsClientYearReportDTO);
}
