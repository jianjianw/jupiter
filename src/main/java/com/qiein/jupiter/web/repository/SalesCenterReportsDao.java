package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.entity.vo.SalesCenterReportsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:xiangliang
 * 销售中心报表
 */
@Repository
public class SalesCenterReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<SalesCenterReportsVO> getSalesCenterReports(ReportsParamVO reportsParamVO, DsInvalidVO dsInvalidVO) {
        List<SalesCenterReportsVO> salesCenterReportsVOS = new ArrayList<>();
        //获取所有门店信息包括目标
        getAllShop(reportsParamVO, salesCenterReportsVOS);
        //获取总成交量
        getSuccessClientCount(reportsParamVO, salesCenterReportsVOS);
        //门店指定成交量
        getShopCallOnSuccessClientCount(reportsParamVO, salesCenterReportsVOS);
        //总客资
        getAllClientCount(reportsParamVO, salesCenterReportsVOS);
        //门市指名待定量
        getPendingClientCount(reportsParamVO, salesCenterReportsVOS, dsInvalidVO);
        //筛选待定量
        getFilterPendingClientCount(reportsParamVO, salesCenterReportsVOS);
        // 筛选中
        getFilterInClientCount(reportsParamVO, salesCenterReportsVOS);
        //筛选无效
        getFilterInValidClientCount(reportsParamVO, salesCenterReportsVOS);
        //有效量
        getValidClientCount(reportsParamVO, salesCenterReportsVOS, dsInvalidVO);
        //计算合计
        computeTotal(salesCenterReportsVOS);
        //计算概率
        computeRate(salesCenterReportsVOS);
        return salesCenterReportsVOS;
    }

    /**
     * 获取所有门店信息包括目标
     */
    private void getAllShop(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT shop.id id,shop.shopname shopName,");
        sb.append(" ifnull(sum(detail.TOTALSUCCESSCOUNTTARGET),0) totalSuccessCountTarget,");
        sb.append(" ifnull(sum(detail.SHOPCALLONSUCCESSCOUNTTARGET),0) shopCallOnSuccessCountTarget,");
        sb.append(" ifnull(sum(detail.SHOPCALLONVALIDCOUNTTARGET),0) shopCallOnValidCountTarget");
        sb.append(" FROM hm_pub_shop shop");
        sb.append(" LEFT JOIN hm_crm_shop_detail detail ON shop.id = detail.shopid AND shop.companyid = detail.companyid and detail.CREATETIME between ? and ?");
        sb.append(" WHERE shop.ISSHOW = 1 AND shop.TYPEID = 1");
        sb.append(" and shop.companyid=?");
        sb.append(" GROUP BY shop.id");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getStart(), reportsParamVO.getEnd(), reportsParamVO.getCompanyId()});
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("id").toString())));
            salesCenterReportsVO.setShopName(String.valueOf(map.get("shopName").toString()));
            salesCenterReportsVO.setTotalSuccessCountTarget(Integer.parseInt(String.valueOf(map.get("totalSuccessCountTarget").toString())));
            salesCenterReportsVO.setShopCallOnSuccessCountTarget(Integer.parseInt(String.valueOf(map.get("shopCallOnSuccessCountTarget").toString())));
            salesCenterReportsVO.setShopCallOnValidCountTarget(Integer.parseInt(String.valueOf(map.get("shopCallOnValidCountTarget").toString())));
            salesCenterReportsVOS.add(salesCenterReportsVO);
        }
    }

    /**
     * 获取基础sql
     */
    private void getBaseSql(StringBuilder sb, ReportsParamVO reportsParamVO, boolean lazy, boolean isCreate) {
        sb.append(" SELECT info.SHOPID shopId,count(1) count FROM hm_crm_client_info info");
        sb.append(" WHERE info.COMPANYID = ? AND info.ISDEL = 0 AND info.SHOPID IS NOT NULL");
        if (StringUtil.isNotEmpty(reportsParamVO.getShopIds())) {
            sb.append(" and info.shopid in (" + reportsParamVO.getShopIds() + ")");
        }
        if (StringUtil.isNotEmpty(reportsParamVO.getType())) {
            sb.append(" and info.typeid in (" + reportsParamVO.getType() + ")");
        }
        if (lazy) {
            sb.append(" and info.srctype in (4)");
        }
        if (isCreate) {
            sb.append(" and info.createtime between ? and ?");
        } else {
            sb.append(" and info.successtime between ? and ?");
            sb.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
        }
    }

    /**
     * 获取总成交量
     */
    private void getSuccessClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, false, false);
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd(), ClientStatusConst.IS_SUCCESS});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setSuccessClientCount(salesCenterReportsVO1.getSuccessClientCount());
                }
            }
        }
    }

    /**
     * 门店指定成交量
     */
    private void getShopCallOnSuccessClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, false);
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd(), ClientStatusConst.IS_SUCCESS});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setShopCallOnSuccessClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setShopCallOnSuccessClientCount(salesCenterReportsVO1.getShopCallOnSuccessClientCount());
                }
            }
        }
    }

    /**
     * 总客资
     */
    private void getAllClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, true);
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setAllClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setAllClientCount(salesCenterReportsVO1.getAllClientCount());
                }
            }
        }
    }

    /**
     * 门市指名待定量
     */
    private void getPendingClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, true);
        sb.append(" and INSTR( '" + dsInvalidVO.getDsDdStatus() + "', CONCAT(',',info.STATUSID + '',',')) != 0");
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setPendingClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setPendingClientCount(salesCenterReportsVO1.getPendingClientCount());
                }
            }
        }
    }

    /**
     * 筛选待定量
     */
    private void getFilterPendingClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, true);
        sb.append("   and info.STATUSID = 98 ");
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setFilterPendingClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setFilterPendingClientCount(salesCenterReportsVO1.getFilterPendingClientCount());
                }
            }
        }
    }

    /**
     * 筛选无效
     */
    private void getFilterInValidClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, true);
        sb.append("   and info.STATUSID = 99 ");
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setFilterInValidClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setFilterInValidClientCount(salesCenterReportsVO1.getFilterInValidClientCount());
                }
            }
        }
    }

    /**
     * 筛选中
     */
    private void getFilterInClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, true);
        sb.append("   and info.STATUSID = 0 ");
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setFilterInClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setFilterInClientCount(salesCenterReportsVO1.getFilterInClientCount());
                }
            }
        }
    }

    /**
     * 有效量
     */
    private void getValidClientCount(ReportsParamVO reportsParamVO, List<SalesCenterReportsVO> salesCenterReportsVOS, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        getBaseSql(sb, reportsParamVO, true, true);
        sb.append(" AND INSTR('" + dsInvalidVO.getZjsValidStatus() + "',CONCAT( ','+info.STATUSID + '', ','))>0 ");
        sb.append(" group by info.shopid");
        List<Map<String, Object>> salesCenterReports = jdbcTemplate.queryForList(sb.toString(),
                new Object[]{reportsParamVO.getCompanyId(), reportsParamVO.getStart(), reportsParamVO.getEnd()});
        List<SalesCenterReportsVO> salesCenterReportsVOSBak = new ArrayList<>();
        for (Map<String, Object> map : salesCenterReports) {
            SalesCenterReportsVO salesCenterReportsVO = new SalesCenterReportsVO();
            salesCenterReportsVO.setShopId(Integer.parseInt(String.valueOf(map.get("shopId").toString())));
            salesCenterReportsVO.setValidClientCount(Integer.parseInt(String.valueOf(map.get("count").toString())));
            salesCenterReportsVOSBak.add(salesCenterReportsVO);
        }
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            for (SalesCenterReportsVO salesCenterReportsVO1 : salesCenterReportsVOSBak) {
                if (salesCenterReportsVO.getShopId()==(salesCenterReportsVO1.getShopId())) {
                    salesCenterReportsVO.setValidClientCount(salesCenterReportsVO1.getValidClientCount());
                }
            }
        }
    }

    /**
     * 计算合计
     *
     * @param salesCenterReportsVOS
     */
    private void computeTotal(List<SalesCenterReportsVO> salesCenterReportsVOS) {
        SalesCenterReportsVO salesCenterReportsTotal = new SalesCenterReportsVO();
        salesCenterReportsTotal.setShopId(-1);
        salesCenterReportsTotal.setShopName("合计");
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            salesCenterReportsTotal.setTotalSuccessCountTarget(salesCenterReportsTotal.getTotalSuccessCountTarget() + salesCenterReportsVO.getTotalSuccessCountTarget());
            salesCenterReportsTotal.setShopCallOnSuccessCountTarget(salesCenterReportsTotal.getShopCallOnSuccessCountTarget() + salesCenterReportsVO.getShopCallOnSuccessCountTarget());
            salesCenterReportsTotal.setShopCallOnValidCountTarget(salesCenterReportsTotal.getShopCallOnValidCountTarget() + salesCenterReportsVO.getShopCallOnValidCountTarget());
            salesCenterReportsTotal.setSuccessClientCount(salesCenterReportsTotal.getSuccessClientCount() + salesCenterReportsVO.getSuccessClientCount());
            salesCenterReportsTotal.setShopCallOnSuccessClientCount(salesCenterReportsTotal.getShopCallOnSuccessClientCount() + salesCenterReportsVO.getShopCallOnSuccessClientCount());
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
    private void computeRate(List<SalesCenterReportsVO> salesCenterReportsVOS) {
        for (SalesCenterReportsVO salesCenterReportsVO : salesCenterReportsVOS) {
            //毛客资计算
            salesCenterReportsVO.setClientCount(salesCenterReportsVO.getAllClientCount() - salesCenterReportsVO.getFilterInClientCount() - salesCenterReportsVO.getFilterInValidClientCount() - salesCenterReportsVO.getFilterPendingClientCount());
            //有效客资挖客率目标
            double validCountWkRateTarget = (double) salesCenterReportsVO.getShopCallOnValidCountTarget() / salesCenterReportsVO.getTotalSuccessCountTarget();
            salesCenterReportsVO.setValidCountWkRateTarget(parseDouble(((Double.isNaN(validCountWkRateTarget) || Double.isInfinite(validCountWkRateTarget)) ? 0.0 : validCountWkRateTarget) * 100));
            //总成交目标完成率
            double totalSuccessClientTargetRate = (double) salesCenterReportsVO.getSuccessClientCount() / salesCenterReportsVO.getTotalSuccessCountTarget();
            salesCenterReportsVO.setTotalSuccessClientTargetRate(parseDouble(((Double.isNaN(totalSuccessClientTargetRate) || Double.isInfinite(totalSuccessClientTargetRate)) ? 0.0 : totalSuccessClientTargetRate) * 100));
            //门市指名成交目标完成率
            double shopCallOnSuccessClientTargetRate = (double) salesCenterReportsVO.getShopCallOnSuccessClientCount() / salesCenterReportsVO.getShopCallOnSuccessCountTarget();
            salesCenterReportsVO.setShopCallOnSuccessClientTargetRate(parseDouble(((Double.isNaN(shopCallOnSuccessClientTargetRate) || Double.isInfinite(shopCallOnSuccessClientTargetRate)) ? 0.0 : shopCallOnSuccessClientTargetRate) * 100));
            //有效客资量目标完成率
            double shopCallOnValidCountTargetRate = (double) salesCenterReportsVO.getValidClientCount() / salesCenterReportsVO.getShopCallOnValidCountTarget();
            salesCenterReportsVO.setShopCallOnValidCountTargetRate(parseDouble(((Double.isNaN(shopCallOnValidCountTargetRate) || Double.isInfinite(shopCallOnValidCountTargetRate)) ? 0.0 : shopCallOnValidCountTargetRate) * 100));
            //总客资有效率
            double validClientRate = (double) salesCenterReportsVO.getValidClientCount() / salesCenterReportsVO.getClientCount();
            salesCenterReportsVO.setValidClientRate(parseDouble(((Double.isNaN(validClientRate) || Double.isInfinite(validClientRate)) ? 0.0 : validClientRate) * 100));
            //总客资待定率
            double pendingClientRate = (double) salesCenterReportsVO.getPendingClientCount() / salesCenterReportsVO.getClientCount();
            salesCenterReportsVO.setPendingClientRate(parseDouble(((Double.isNaN(pendingClientRate) || Double.isInfinite(pendingClientRate)) ? 0.0 : pendingClientRate) * 100));
            //有效客资挖客率
            double validCountWkRate = (double) salesCenterReportsVO.getValidClientCount() / salesCenterReportsVO.getSuccessClientCount();
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
