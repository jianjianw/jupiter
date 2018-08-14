package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ChinaTerritoryConst;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;
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
 * 市域分析报表
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/14 11:18
 */
@Repository
public class CityReportsDao {

    private static final String PLACEHOLDER = "$Dy$UpUp$";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取市域分析报表
     *
     * @param citiesAnalysisParamDTO
     * @return
     */
    public List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO) {
        List<RegionReportsVO> resultContent = new ArrayList<>();
        //从map中找到省份下属的城市
        for (String cityName : citiesAnalysisParamDTO.getProvinceNames().split(",")) {
            Object[] objs = {};  //TODO 将这个加上参数
            //TODO 下面这两个选一个
            RegionReportsVO cityReports1 = jdbcTemplate.queryForObject(getCityReportSQL(citiesAnalysisParamDTO,cityName),objs,RegionReportsVO.class);
//            List<RegionReportsVO> cityReports2 = jdbcTemplate.query(getCityReportSQL(citiesAnalysisParamDTO,cityName),
//                    objs,
//                    new  RowMapper<RegionReportsVO>(){
//                        /**
//                         * 将返回的结果集转换成指定对象
//                         * @param rs
//                         * @param i
//                         * @return
//                         * @throws SQLException
//                         */
//                        @Override
//                        public RegionReportsVO mapRow(ResultSet rs, int i) throws SQLException {
//                            RegionReportsVO regionReportsVO = new RegionReportsVO();
//                            regionReportsVO.setAllClientCount(rs.getInt("allClientCount"));
//                            regionReportsVO.setPendingClientCount(rs.getInt("pendingClientCount"));
//                            regionReportsVO.setFilterPendingClientCount(rs.getInt("filterPendingClientCount"));
//                            regionReportsVO.setInValidClientCount(rs.getInt("inValidClientCount"));
//                            regionReportsVO.setFilterInValidClientCount(rs.getInt("filterInValidClientCount"));
//                            regionReportsVO.setComeShopClientCount(rs.getInt("comeShopClientCount"));
//                            regionReportsVO.setSuccessClientCount(rs.getInt("successClientCount"));
//                            regionReportsVO.setAvgAmount(rs.getInt(("avgAmount")));
//                            regionReportsVO.setAmount(rs.getInt("amount"));
//                            return regionReportsVO;
//                        }
//                    });

            //将该市报表加入返回结果列表中
            resultContent.add(cityReports1);
        }
        return resultContent;
    }

    /**
     * 获取城市分析报表sql
     *
     * @param citiesAnalysisParamDTO
     * @return
     */
    private String getCityReportSQL(CitiesAnalysisParamDTO citiesAnalysisParamDTO ,String cityName) {
        StringBuilder sql = new StringBuilder();
        //总客资  待定量 筛选待定 无效量 筛选无效量 入店量 成交量 成交均价 营业额
        sql.append("SELECT ")
                .append("zkz.allClientCount , ddl.pendingClientCount , sxdd.filterPendingClientCount , wxl.inValidClientCount , sxwxl.filterInValidClientCount , rdl.comeShopClientCount , cjl.successClientCount , cjjj.avgAmount , yye.amount ")
                .append("FROM")
                .append("(" + getAllClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") zkz ,") //总客资
                .append("(" + getPendingClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") ddl ,") //待定量
                .append("(" + getFilterPendingClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") sxdd ,") //筛选待定
                .append("(" + getInValidClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") wxl ,") //无效量
                .append("(" + getFilterInValidClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") sxwxl ,")   //筛选无效量
                .append("(" + getComeShopClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") rdl ,") //入店量
                .append("(" + getSuccessClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") cjl ,") //成交量
                .append("(" + getAvgAmount(citiesAnalysisParamDTO.getCompanyId()) + ") cjjj ,")    //成交均价
                .append("(" + getAmount(citiesAnalysisParamDTO.getCompanyId()) + ") yye ,");    //营业额
        //TODO 将所有字段的子查询写完后，将其中的占位符替换成指定条件，然后执行
        setConditionSQL(sql, citiesAnalysisParamDTO.getSearchClientType());
        return sql.toString();
    }

    /**
     * 基础SQL
     *
     * @param sb
     * @param companyId
     * @return
     */
    private StringBuilder getBaseSQL(StringBuilder sb, int companyId, String alias) {
        sb.append("SELECT COUNT(1) " + alias)
                .append("FROM hm_crm_client_info_" + companyId + " info , hm_crm_client_detail_" + companyId + " detail")
                .append("WHERE info.KZID = detail.KZID AND info.ISDEL = 0 AND info.COMPANYID = " + companyId);
        return sb;
    }

    /**
     * 总客资
     *
     * @param companyId
     * @return
     */
    private StringBuilder getAllClientCount(int companyId) {
        StringBuilder allClientSQL = new StringBuilder();
        //获取基础sql 子句
        getBaseSQL(allClientSQL,companyId,"allClientCount");
        //TODO 后续sql 留在笔记本的navicat里，白天加上
        return allClientSQL;
    }

    /**
     * 待定量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getPendingClientCount(int companyId) {
        StringBuilder pendingClientSQL = new StringBuilder();
        getBaseSQL(pendingClientSQL,companyId,"allClientCount");

        return pendingClientSQL;
    }

    /**
     * 筛选待定
     *
     * @param companyId
     * @return
     */
    private StringBuilder getFilterPendingClientCount(int companyId) {
        StringBuilder filterPendingClientSQL = new StringBuilder();
        getBaseSQL(filterPendingClientSQL,companyId,"allClientCount");

        return filterPendingClientSQL;
    }

    /**
     * 无效量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getInValidClientCount(int companyId) {
        StringBuilder inValidClientSQL = new StringBuilder();
        getBaseSQL(inValidClientSQL,companyId,"allClientCount");

        return inValidClientSQL;
    }

    /**
     * 筛选无效量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getFilterInValidClientCount(int companyId) {
        StringBuilder filterInValidClientSQL = new StringBuilder();
        getBaseSQL(filterInValidClientSQL,companyId,"allClientCount");

        return filterInValidClientSQL;
    }

    /**
     * 入店量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getComeShopClientCount(int companyId) {
        StringBuilder comeShopClientSQL = new StringBuilder();
        getBaseSQL(comeShopClientSQL,companyId,"allClientCount");

        return comeShopClientSQL;
    }

    /**
     * 成交量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getSuccessClientCount(int companyId) {
        StringBuilder successClientSQL = new StringBuilder();
        getBaseSQL(successClientSQL,companyId,"allClientCount");

        return successClientSQL;
    }

    /**
     * 成交均价
     *
     * @param companyId
     * @return
     */
    private StringBuilder getAvgAmount(int companyId) {
        StringBuilder avgAmountSQL = new StringBuilder();
        getBaseSQL(avgAmountSQL,companyId,"allClientCount");

        return avgAmountSQL;
    }

    /**
     * 营业额
     *
     * @param companyId
     * @return
     */
    private StringBuilder getAmount(int companyId) {
        StringBuilder amountSQL = new StringBuilder();
        getBaseSQL(amountSQL,companyId,"allClientCount");

        return amountSQL;
    }

    /**
     * 将sql字符串中所有的占位符替换成指定条件
     *
     * @param sb
     * @param searchClientType
     * @return
     */
    private StringBuilder setConditionSQL(StringBuilder sb, int searchClientType) {//查询的客资类型 1所有客资 2 电话客资 3 微信客资 4 qq客资 5 有电话有微信 6 无电话 7.无微信 8 只有电话 9只有微信 10 只有qq
        String condition = null;
        switch (searchClientType) {
            case 1: //1所有客资
                break;
            case 2: //2电话客资
                condition = " AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') ";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 3: //3微信客资
                condition = " AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') ";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 4: //4qq客资
                condition = " AND (info.KZQQ IS NOT NULL AND info.KZQQ != '') ";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 5: //5有电话有微信
                condition = " AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') ";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 6: //无电话
                condition = " AND (info.KZPHONE IS NULL OR info.KZPHONE == '') ";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 7: //7无微信
                condition = " AND (info.KZWECHAT IS NULL OR info.KZWECHAT == '') ";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 8: //8只有电话
                condition = " AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') AND (info.KZWECHAT IS NULL OR info.KZWECHAT == '') AND (info.KZQQ IS NULL OR info.KZQQ == '')";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 9: //9只有微信
                condition = " AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') AND (info.KZPHONE IS NULL OR info.KZPHONE == '') AND (info.KZQQ IS NULL OR info.KZQQ == '')";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            case 10://10只有qq
                condition = " AND (info.KZQQ IS NOT NULL AND info.KZQQ != '') AND (info.KZPHONE IS NULL OR info.KZPHONE == '') AND (info.KZWECHAT IS NULL OR info.KZWECHAT == '')";
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition);
                }
                break;
            default:
                break;
        }
        System.out.println("输出本次最终sql: " + sb.toString());
        return sb;
    }
}
