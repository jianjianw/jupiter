package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ProfessionalCenterVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 专业中心报表
 * author:xiangliang
 */
@Repository
public class ProfessionalCenterDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 获取报表
     */
    public List<ProfessionalCenterVO> getProfessionalCenterVO(ReportsParamVO reportsParamVO, DsInvalidVO dsInvalidVO){
        List<ProfessionalCenterVO> professionalCenterVOS=new ArrayList<>();
        //获取所有专业中心渠道
        getAllSrc(reportsParamVO,professionalCenterVOS);
        //获取总客资
        getAllClientCount(reportsParamVO,professionalCenterVOS);
        //获取待定量
        getPendingClientCount(reportsParamVO,professionalCenterVOS,dsInvalidVO);
        //获取筛选待定量
        getFilterPendingClientCount(reportsParamVO,professionalCenterVOS);
        //获取筛选无效量
        getFilterInValidClientCount(reportsParamVO,professionalCenterVOS);
        //获取筛选中量
        getFilterInClientCount(reportsParamVO,professionalCenterVOS);
        //获取有销量
        getValidClientCount(reportsParamVO,professionalCenterVOS,dsInvalidVO);
        //获取成交量
        getSuccessClientCount(reportsParamVO,professionalCenterVOS);
        //计算合计
        computeTotal(professionalCenterVOS);
        //计算概率
        computeRate(professionalCenterVOS);


        return professionalCenterVOS;
    }
    private void getAllSrc(ReportsParamVO reportsParamVO,List<ProfessionalCenterVO> professionalCenterVOS){
        StringBuilder sb =new StringBuilder();
        sb.append(" SELECT src.ID id,src.SRCIMG srcImg,src.SRCNAME srcName,");
        sb.append(" IFNULL(sum(detail.TOTALSHOOTINGTARGET),0) totalShootingTarget,");
        sb.append(" IFNULL(sum(detail.VALIDCOUNTTARGET), 0) validCountTarget,");
        sb.append(" IFNULL(sum(detail.TOTALSHOOTING), 0) totalShooting");
        sb.append(" FROM hm_crm_source src");
        sb.append(" LEFT JOIN hm_crm_shop_detail detail ON detail.COMPANYID = src.COMPANYID AND detail.type = 2 AND detail.CREATETIME BETWEEN ? AND ? AND detail.SHOPID = src.ID");
        sb.append(" WHERE src.CHANNELID = 114  and src.COMPANYID=?");
        if(StringUtil.isNotEmpty(reportsParamVO.getSourceIds())){
            sb.append(" and src.id in ("+reportsParamVO.getSourceIds()+")");
        }
        sb.append(" group by src.id");
        sb.append(" order by src.srcname");
        List<Map<String,Object>> professionalCenterReports=jdbcTemplate.queryForList(sb.toString(),new Object[]{reportsParamVO.getStart(),reportsParamVO.getEnd(),reportsParamVO.getCompanyId()});
        for(Map<String,Object> map:professionalCenterReports){
            ProfessionalCenterVO professionalCenterVO=new ProfessionalCenterVO();
            professionalCenterVO.setSrcId(Integer.parseInt(String.valueOf(map.get("id").toString())));
            professionalCenterVO.setSrcName(String.valueOf(map.get("srcName").toString()));
            professionalCenterVO.setTotalShootingTarget(Integer.parseInt(String.valueOf(map.get("totalShootingTarget").toString())));
            professionalCenterVO.setValidCountTarget(Integer.parseInt(String.valueOf(map.get("validCountTarget").toString())));
            professionalCenterVO.setTotalShooting(Integer.parseInt(String.valueOf(map.get("totalShooting").toString())));
            professionalCenterVOS.add(professionalCenterVO);
        }


    }
    private void getBaseSql(StringBuilder sb,ReportsParamVO reportsParamVO,boolean isCreate){
        sb.append(" SELECT count(1) count,info.SOURCEID srcId FROM hm_crm_client_info info");
        sb.append(" LEFT JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID");
        sb.append(" where  src.CHANNELID = 114 and info.companyid=?");
        if(StringUtil.isNotEmpty(reportsParamVO.getType())){
            sb.append(" and info.typeid in ("+reportsParamVO.getType()+")");
        }
        if (isCreate) {
            sb.append(" and info.createtime between ? and ?");
        } else {
            sb.append(" and info.successtime between ? and ?");
            sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        }
    }
    /**
     * 总客资
     */
    private void getAllClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,true);
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("srcId").toString())));
            salesCenterReportsVO.setAllClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setAllClientCount(salesCenterReportsVO1.getAllClientCount());
                }
            }
        }
    }

    /**
     * 待定量
     */
    private void getPendingClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,true);
        sb.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("srcId").toString())));
            salesCenterReportsVO.setPendingClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setPendingClientCount(salesCenterReportsVO1.getPendingClientCount());
                }
            }
        }
    }

    /**
     * 筛选待定量
     */
    private void getFilterPendingClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,true);
        sb.append("   and info.STATUSID = 98 ");
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("srcId").toString())));
            salesCenterReportsVO.setFilterPendingClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setFilterPendingClientCount(salesCenterReportsVO1.getFilterPendingClientCount());
                }
            }
        }
    }

    /**
     * 筛选无效
     */
    private void getFilterInValidClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,true);
        sb.append("   and info.STATUSID = 99 ");
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setFilterInValidClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setFilterInValidClientCount(salesCenterReportsVO1.getFilterInValidClientCount());
                }
            }
        }
    }

    /**
     * 筛选中
     */
    private void getFilterInClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,true);
        sb.append("   and info.STATUSID = 0 ");
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setFilterInClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setFilterInClientCount(salesCenterReportsVO1.getFilterInClientCount());
                }
            }
        }
    }

    /**
     * 有效量
     */
    private void getValidClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,true);
        sb.append(" AND INSTR('" + dsInvalidVO.getZjsValidStatus() + "',CONCAT( ','+info.STATUSID + '', ','))>0 ");
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setValidClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setValidClientCount(salesCenterReportsVO1.getValidClientCount());
                }
            }
        }
    }
    /**
     * 成交量
     */
    private void getSuccessClientCount(ReportsParamVO reportsParamVO, List<ProfessionalCenterVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb,reportsParamVO,false);
        sb.append(" GROUP BY info.SOURCEID");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd(), ClientStatusConst.IS_SUCCESS});
        List<ProfessionalCenterVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            ProfessionalCenterVO salesCenterReportsVO = new ProfessionalCenterVO();
            salesCenterReportsVO.setSrcId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (ProfessionalCenterVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getSrcId()==(salesCenterReportsVO1.getSrcId())) {
                    salesCenterReportsVO.setSuccessClientCount(salesCenterReportsVO1.getSuccessClientCount());
                }
            }
        }
    }
    /**
     * 计算合计
     *
     * @param salesCenterReportsVOS
     */
    private void computeTotal(List<ProfessionalCenterVO> salesCenterReportsVOS) {
        ProfessionalCenterVO salesCenterReportsTotal = new ProfessionalCenterVO();
        salesCenterReportsTotal.setSrcId(-1);
        salesCenterReportsTotal.setSrcName("合计");
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            salesCenterReportsTotal.setTotalShootingTarget(salesCenterReportsTotal.getTotalShootingTarget() + salesCenterReportsVO.getTotalShootingTarget());
            salesCenterReportsTotal.setValidCountTarget(salesCenterReportsTotal.getValidCountTarget() + salesCenterReportsVO.getValidCountTarget());
            salesCenterReportsTotal.setTotalShooting(salesCenterReportsTotal.getTotalShooting() + salesCenterReportsVO.getTotalShooting());
            salesCenterReportsTotal.setSuccessClientCount(salesCenterReportsTotal.getSuccessClientCount() + salesCenterReportsVO.getSuccessClientCount());
            salesCenterReportsTotal.setAllClientCount(salesCenterReportsTotal.getAllClientCount() + salesCenterReportsVO.getAllClientCount());
            salesCenterReportsTotal.setPendingClientCount(salesCenterReportsTotal.getPendingClientCount() + salesCenterReportsVO.getPendingClientCount());
            salesCenterReportsTotal.setFilterPendingClientCount(salesCenterReportsTotal.getFilterPendingClientCount() + salesCenterReportsVO.getFilterPendingClientCount());
            salesCenterReportsTotal.setFilterInValidClientCount(salesCenterReportsTotal.getFilterInValidClientCount() + salesCenterReportsVO.getFilterInValidClientCount());
            salesCenterReportsTotal.setFilterInClientCount(salesCenterReportsTotal.getFilterInClientCount() + salesCenterReportsVO.getFilterInClientCount());
            salesCenterReportsTotal.setValidClientCount(salesCenterReportsTotal.getValidClientCount() + salesCenterReportsVO.getValidClientCount());
        }
        salesCenterReportsVOS.add(0, salesCenterReportsTotal);
    }

    /**
     * 计算概率
     *
     * @param salesCenterReportsVOS
     */
    private void computeRate(List<ProfessionalCenterVO> salesCenterReportsVOS) {
        for (ProfessionalCenterVO salesCenterReportsVO : salesCenterReportsVOS) {
            //毛客资计算
            salesCenterReportsVO.setClientCount(salesCenterReportsVO.getAllClientCount() - salesCenterReportsVO.getFilterInClientCount() - salesCenterReportsVO.getFilterInValidClientCount() - salesCenterReportsVO.getFilterPendingClientCount());
            //有效客资挖客率目标
            double validCountWkRateTarget = (double) salesCenterReportsVO.getValidCountTarget() / salesCenterReportsVO.getTotalShootingTarget();
            salesCenterReportsVO.setValidCountWkRateTarget(parseDouble(((Double.isNaN(validCountWkRateTarget) || Double.isInfinite(validCountWkRateTarget)) ? 0.0 : validCountWkRateTarget) * 100));
            //有效客资量目标完成率
            double shopCallOnValidCountTargetRate = (double) salesCenterReportsVO.getValidClientCount() / salesCenterReportsVO.getValidCountTarget();
            salesCenterReportsVO.setValidCountTargetRate(parseDouble(((Double.isNaN(shopCallOnValidCountTargetRate) || Double.isInfinite(shopCallOnValidCountTargetRate)) ? 0.0 : shopCallOnValidCountTargetRate) * 100));
            //总拍摄目标完成率
            double totalShootingTargetRate = (double) salesCenterReportsVO.getTotalShooting() / salesCenterReportsVO.getTotalShootingTarget();
            salesCenterReportsVO.setTotalShootingTargetRate(parseDouble(((Double.isNaN(totalShootingTargetRate) || Double.isInfinite(totalShootingTargetRate)) ? 0.0 : totalShootingTargetRate) * 100));
            //总客资有效率
            double validClientRate = (double) salesCenterReportsVO.getValidClientCount() / salesCenterReportsVO.getClientCount();
            salesCenterReportsVO.setValidClientRate(parseDouble(((Double.isNaN(validClientRate) || Double.isInfinite(validClientRate)) ? 0.0 : validClientRate) * 100));
            //总客资待定率
            double pendingClientRate = (double) salesCenterReportsVO.getPendingClientCount() / salesCenterReportsVO.getClientCount();
            salesCenterReportsVO.setPendingClientRate(parseDouble(((Double.isNaN(pendingClientRate) || Double.isInfinite(pendingClientRate)) ? 0.0 : pendingClientRate) * 100));
            //有效客资挖客率
            double validCountWkRate = (double) salesCenterReportsVO.getValidClientCount() / salesCenterReportsVO.getTotalShooting();
            salesCenterReportsVO.setValidCountWkRate(parseDouble(((Double.isNaN(validCountWkRate) || Double.isInfinite(validCountWkRate)) ? 0.0 : validCountWkRate) * 100));
        }


    }

    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }
}
