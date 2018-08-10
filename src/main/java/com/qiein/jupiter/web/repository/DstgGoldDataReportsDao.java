package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgGoldDataReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
@Repository
public class DstgGoldDataReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommonReportsDao commonReportsDao;

    /**
     * 获取电商推广广告信息汇总报表
     */
    public List<DstgGoldDataReportsVO> getDstgGoldDataReprots(ReportsParamVO reportsParamVO,DsInvalidVO invalidConfig) {
        List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS = new ArrayList<>();
        // 获取总客资
        getAllClientCount(reportsParamVO, dstgGoldDataReportsVOS);
        //获取待定量
        getWaitClientCount(reportsParamVO,dstgGoldDataReportsVOS,invalidConfig);
        return dstgGoldDataReportsVOS;
    }


    /**
     * 获取广告总量
     */
    private void getAllClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS) {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select IFNULL(detail.adid,'其他') as adid,count(detail.kzid) as all_client_count ");
        sb.append(" from");
        sb.append(infoTabName + " info ," + detailTabName + " detail");
        sb.append(" where");
        sb.append(" info.kzid = detail.kzid");
        sb.append(" and info.isdel = 0");
        sb.append(" and info.companyid = ?");
        sb.append(" and (info.CREATETIME BETWEEN ? AND ? or info.COMESHOPTIME BETWEEN ? AND ? or info.SUCCESSTIME BETWEEN ? AND ?)");
        sb.append(" group by detail.adid");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd()});
        // 处理数据
        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
            dstgGoldDataReportsVO.setAllClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("all_client_count"))));
            dstgGoldDataReportsVOS.add(dstgGoldDataReportsVO);
        }
    }

    /**
     * 获取待定量
     */
    private void getWaitClientCount(ReportsParamVO reportsParamVO, List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS,DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        String infoTabName = DBSplitUtil.getInfoTabName(reportsParamVO.getCompanyId());
        String detailTabName = DBSplitUtil.getDetailTabName(reportsParamVO.getCompanyId());
        sb.append(" select IFNULL(detail.adid,'其他') as adid,count(detail.kzid) as all_client_count ");
        sb.append(" from");
        sb.append(infoTabName + " info ," + detailTabName + " detail");
        sb.append(" where");
        sb.append(" info.kzid = detail.kzid");
        sb.append(" and info.isdel = 0");
        sb.append(" and info.companyid = ?");
        sb.append(" and (info.CREATETIME BETWEEN ? AND ? or info.COMESHOPTIME BETWEEN ? AND ? or info.SUCCESSTIME BETWEEN ? AND ?)");
        sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" group by detail.adid");
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd(),
                        reportsParamVO.getStart(),
                        reportsParamVO.getEnd(),
                        dsInvalidVO.getDsDdStatus() });

        // 处理数据

        //FIXME 此处处理数据有问题
//        for (Map<String, Object> dstgGoldDataReport : dstgGoldDataReports) {
//            DstgGoldDataReportsVO dstgGoldDataReportsVO = new DstgGoldDataReportsVO();
//            dstgGoldDataReportsVO.setAdId((String) dstgGoldDataReport.get("adid"));
//            dstgGoldDataReportsVO.setWaitClientCount(Integer.parseInt(Long.toString((Long) dstgGoldDataReport.get("all_client_count"))));
//            dstgGoldDataReportsVOS.add(dstgGoldDataReportsVO);
//        }

    }



}
