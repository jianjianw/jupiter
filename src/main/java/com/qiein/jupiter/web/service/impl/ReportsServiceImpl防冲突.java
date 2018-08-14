package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;
import com.qiein.jupiter.web.repository.CityReportsDao;
import com.qiein.jupiter.web.repository.CommonReportsDao;
import com.qiein.jupiter.web.service.ReportsService防冲突;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 市域分析报表
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/14 11:00
 */
public class ReportsServiceImpl防冲突 implements ReportsService防冲突 {

    @Autowired
    CommonReportsDao commonReportsDao;

    @Autowired
    CityReportsDao cityReportsDao;

    @Override
    public List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO) {
        //获取公司自定义的无效设置
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(citiesAnalysisParamDTO.getCompanyId());
        //获取市域分析报表
        List<RegionReportsVO> cityReport = cityReportsDao.getCityReport(citiesAnalysisParamDTO);
        return cityReport;
    }
}
