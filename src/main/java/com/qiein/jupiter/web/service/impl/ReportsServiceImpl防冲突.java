package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ZjsClientYearReportDTO;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.repository.CityReportsDao;
import com.qiein.jupiter.web.repository.CommonReportsDao;
import com.qiein.jupiter.web.repository.ProvinceReportsDao;
import com.qiein.jupiter.web.repository.ZjsKzOfYearDao;
import com.qiein.jupiter.web.service.ReportsService防冲突;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 市域分析报表
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/14 11:00
 */
@Service
public class ReportsServiceImpl防冲突 implements ReportsService防冲突 {

    @Autowired
    CommonReportsDao commonReportsDao;

    @Autowired
    CityReportsDao cityReportsDao;

    @Autowired
    ProvinceReportsDao provinceReportsDao;

    @Autowired
    ZjsKzOfYearDao zjsKzOfYearDao;

    @Override
    public List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO) {
        //获取公司自定义的无效设置
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(citiesAnalysisParamDTO.getCompanyId());
        //获取市域分析报表
        List<RegionReportsVO> cityReport = cityReportsDao.getCityReport(citiesAnalysisParamDTO, invalidConfig);
        return cityReport;
    }

    @Override
    public List<ProvinceReportsVO2> getProvinceReport(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {

        //获取公司自定义的无效设置 TODO 其实部分数据是不用调这个借口
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(provinceAnalysisParamDTO.getCompanyId());
        List<ProvinceReportsVO2> provinceReport = provinceReportsDao.provinceReport(provinceAnalysisParamDTO, invalidConfig);
        return provinceReport;
    }

    @Override
    public List<ZjsClientYearReportVO2> getZjsYearReport(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(zjsClientYearReportDTO.getCompanyId());
        List<ZjsClientYearReportVO2> list = zjsKzOfYearDao.getZjsKzYearReport(zjsClientYearReportDTO, invalidConfig);
        return list;
    }

    @Override
    public List<Map<String, Object>> getZjsYearDetailReport(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(zjsClientYearReportDTO.getCompanyId());
        List<Map<String, Object>> list =zjsKzOfYearDao.getZjsYearDetailReport(zjsClientYearReportDTO,invalidConfig);
        return list;
    }
}
