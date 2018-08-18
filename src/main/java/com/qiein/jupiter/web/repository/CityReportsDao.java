package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ChinaTerritoryConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
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
    public List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        List<RegionReportsVO> resultContent = new ArrayList<>();
        //从map中找到省份下属的城市
        int start = citiesAnalysisParamDTO.getStart();
        int end = citiesAnalysisParamDTO.getEnd();
        for (String provinceNames : citiesAnalysisParamDTO.getProvinceNames().split(",")) {

            for (String regionName : ChinaTerritoryConst.TERRITORY_MAP.get(provinceNames).split(",")) {
                Object[] objs = {regionName, start, end,
                        regionName, start, end,
                        regionName, start, end, dsInvalidVO.getDsDdStatus(),
                        regionName, start, end,
                        regionName, start, end,
                        regionName, start, end,
                        regionName, start, end,
                        regionName, start, end,
                        regionName, start, end,
                        regionName, start, end,};
                resultContent.addAll(jdbcTemplate.query(getCityReportSQL(citiesAnalysisParamDTO, dsInvalidVO, regionName),
                        objs,
                        new RowMapper<RegionReportsVO>() {
                            /**
                             * 将返回的结果集转换成指定对象
                             * @param rs
                             * @param i
                             * @return
                             * @throws SQLException
                             */
                            @Override
                            public RegionReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                                RegionReportsVO regionReportsVO = new RegionReportsVO();
                                regionReportsVO.setRegionName(rs.getString("regionName"));
                                regionReportsVO.setAllClientCount(rs.getInt("allClientCount"));
                                regionReportsVO.setPendingClientCount(rs.getInt("pendingClientCount"));
                                regionReportsVO.setFilterPendingClientCount(rs.getInt("filterPendingClientCount"));
                                regionReportsVO.setInValidClientCount(rs.getInt("inValidClientCount"));
                                regionReportsVO.setFilterInValidClientCount(rs.getInt("filterInValidClientCount"));
                                regionReportsVO.setComeShopClientCount(rs.getInt("comeShopClientCount"));
                                regionReportsVO.setSuccessClientCount(rs.getInt("successClientCount"));
                                regionReportsVO.setFilterInClientCount(rs.getInt("filterInClientCount"));
                                regionReportsVO.setAvgAmount(rs.getInt(("avgAmount")));
                                regionReportsVO.setAmount(rs.getInt("amount"));
                                return regionReportsVO;
                            }
                        }));
            }
            //有效量 各种率 合计
            calculate(resultContent,dsInvalidVO);
        }

        return resultContent;
    }

    /**
     * 获取城市分析报表sql
     *
     * @param citiesAnalysisParamDTO
     * @param dsInvalidVO
     * @return
     */
    private String getCityReportSQL(CitiesAnalysisParamDTO citiesAnalysisParamDTO, DsInvalidVO dsInvalidVO, String regionName) {
        StringBuilder sql = new StringBuilder();
        //总客资  待定量 筛选待定 无效量 筛选无效量 入店量 成交量 成交均价 营业额
        sql.append("SELECT ")
                .append("'" + regionName + "' regionName ,zkz.allClientCount , ddl.pendingClientCount , sxdd.filterPendingClientCount ,sxz.filterInClientCount , wxl.inValidClientCount , sxwxl.filterInValidClientCount , rdl.comeShopClientCount , cjl.successClientCount , cjjj.avgAmount , yye.amount ")
                .append(" FROM ")
                .append("(" + getAllClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") zkz ,") //总客资
                .append("(" + getPendingClientCount(citiesAnalysisParamDTO.getCompanyId(), dsInvalidVO) + ") ddl ,") //待定量
                .append("(" + getFilterPendingClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") sxdd ,") //筛选待定
                .append("(" + getFilterInClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") sxz ,") //筛选中
                .append("(" + getInValidClientCount(citiesAnalysisParamDTO.getCompanyId(), dsInvalidVO) + ") wxl ,") //无效量
                .append("(" + getFilterInValidClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") sxwxl ,")   //筛选无效量
                .append("(" + getComeShopClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") rdl ,") //入店量
                .append("(" + getSuccessClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") cjl ,") //成交量
                .append("(" + getAvgAmount(citiesAnalysisParamDTO.getCompanyId()) + ") cjjj ,")    //成交均价
                .append("(" + getAmount(citiesAnalysisParamDTO.getCompanyId()) + ") yye ");    //营业额
        //将其中的占位符替换成指定条件
        setConditionSQL(sql, citiesAnalysisParamDTO);
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
        sb.append(" SELECT COUNT(1) " + alias)
                .append(" FROM hm_crm_client_info_" + companyId + " info , hm_crm_client_detail_" + companyId + " detail ")
                .append(" WHERE info.KZID = detail.KZID AND info.ISDEL = 0 AND info.COMPANYID = " + companyId);
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
        getBaseSQL(allClientSQL, companyId, "allClientCount");
        allClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND info.CREATETIME BETWEEN ? AND ? ")
                .append(PLACEHOLDER);
        return allClientSQL;
    }

    /**
     * 待定量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getPendingClientCount(int companyId, DsInvalidVO dsInvalidVO) {
        StringBuilder pendingClientSQL = new StringBuilder();
        getBaseSQL(pendingClientSQL, companyId, "pendingClientCount");
        pendingClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(" AND INSTR( ? , CONCAT(',',info.STATUSID + '',',')) != 0")
                .append(PLACEHOLDER);
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
        getBaseSQL(filterPendingClientSQL, companyId, "filterPendingClientCount");
        filterPendingClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND info.CLASSID = 1 and info.STATUSID = 98 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);
        return filterPendingClientSQL;
    }

    /**
     * 筛选中
     * @param companyId
     * @return
     */
    private StringBuilder getFilterInClientCount(int companyId){
        StringBuilder filterInClientSQL = new StringBuilder();
        getBaseSQL(filterInClientSQL,companyId,"filterInClientCount");
        filterInClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" and info.CLASSID = 1 and info.STATUSID = 0 ")
                .append(" AND info.CREATETIME BETWEEN ? AND ?");
        return filterInClientSQL;
    }

    /**
     * 无效量
     *
     * @param companyId
     * @return
     */
    private StringBuilder getInValidClientCount(int companyId, DsInvalidVO dsInvalidVO) {
        StringBuilder inValidClientSQL = new StringBuilder();
        getBaseSQL(inValidClientSQL, companyId, "inValidClientCount");
        inValidClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ");
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            inValidClientSQL.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
            inValidClientSQL.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            inValidClientSQL.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ")");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            inValidClientSQL.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
        }
        inValidClientSQL.append(PLACEHOLDER);
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
        getBaseSQL(filterInValidClientSQL, companyId, "filterInValidClientCount");
        filterInValidClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" and info.CLASSID = 6 and info.STATUSID = 99 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);

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
        getBaseSQL(comeShopClientSQL, companyId, "comeShopClientCount");
        comeShopClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND (info.COMESHOPTIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);
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
        getBaseSQL(successClientSQL, companyId, "successClientCount");
        successClientSQL.append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND (info.SUCCESSTIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);
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
        avgAmountSQL.append("SELECT avg(detail.AMOUNT) avgAmount ")
                .append(" FROM hm_crm_client_info_" + companyId + " info , hm_crm_client_detail_" + companyId + " detail ")
                .append(" WHERE info.KZID = detail.KZID AND info.ISDEL = 0 AND info.COMPANYID = " + companyId)
                .append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append("AND info.SUCCESSTIME BETWEEN ? AND ?");
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
        amountSQL.append("SELECT SUM(detail.AMOUNT) amount ")
                .append(" FROM hm_crm_client_info_" + companyId + " info , hm_crm_client_detail_" + companyId + " detail ")
                .append(" WHERE info.KZID = detail.KZID AND info.ISDEL = 0 AND info.COMPANYID = " + companyId)
                .append(" AND INSTR(detail.ADDRESS, ? )>0 ")
                .append(" AND info.SUCCESSTIME BETWEEN ? AND ? ");
        return amountSQL;
    }

    /**
     * 将sql字符串中所有的占位符替换成指定条件
     *
     * @param sb
     * @param searchKey
     * @return
     */
    private StringBuilder setConditionSQL(StringBuilder sb, CitiesAnalysisParamDTO searchKey) {//查询的客资类型 1所有客资 2 电话客资 3 微信客资 4 qq客资 5 有电话有微信 6 无电话 7.无微信 8 只有电话 9只有微信 10 只有qq

        StringBuilder condition = new StringBuilder(StringUtil.isNotEmpty(searchKey.getDicCodes())?" AND info.TYPEID IN ("+searchKey.getDicCodes()+") ":"");
        switch (searchKey.getSearchClientType()) {
            case 1: //1所有客资
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 2: //2电话客资
                condition.append(" AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 3: //3微信客资
                condition.append(" AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 4: //4qq客资
                condition.append(" AND (info.KZQQ IS NOT NULL AND info.KZQQ != '') ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 5: //5有电话有微信
                condition.append(" AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 6: //无电话
                condition.append(" AND (info.KZPHONE IS NULL OR info.KZPHONE = '') ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 7: //7无微信
                condition.append(" AND (info.KZWECHAT IS NULL OR info.KZWECHAT = '') ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 8: //8只有电话
                condition.append(" AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') AND (info.KZWECHAT IS NULL OR info.KZWECHAT = '') AND (info.KZQQ IS NULL OR info.KZQQ = '')");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 9: //9只有微信
                condition.append(" AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') AND (info.KZPHONE IS NULL OR info.KZPHONE = '') AND (info.KZQQ IS NULL OR info.KZQQ = '')");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 10://10只有qq
                condition.append(" AND (info.KZQQ IS NOT NULL AND info.KZQQ != '') AND (info.KZPHONE IS NULL OR info.KZPHONE = '') AND (info.KZWECHAT IS NULL OR info.KZWECHAT = '')");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            default:
                throw new RException(ExceptionEnum.SEARCH_CLIENT_TYPE_IS_UNKNOW);
        }
        System.out.println("输出本次最终sql: " + sb.toString());
        return sb;
    }
//缺筛选中
    /**
     *  计算 有效量 各种率 合计
     */
    private void calculate(List<RegionReportsVO> list, DsInvalidVO invalidConfig){
        RegionReportsVO total = new RegionReportsVO();
        total.setRegionName("合计");
        for (RegionReportsVO rrv:list){
            //有效量
            if(invalidConfig.getDdIsValid()){
                rrv.setValidClientCount(rrv.getAllClientCount()-rrv.getInValidClientCount()-rrv.getFilterInClientCount()-rrv.getFilterInValidClientCount()-rrv.getFilterPendingClientCount());
            }else{
                rrv.setValidClientCount(rrv.getAllClientCount()-rrv.getPendingClientCount()-rrv.getInValidClientCount()-rrv.getFilterInClientCount()-rrv.getFilterInValidClientCount()-rrv.getFilterPendingClientCount());
            }
            //客资量(总客资-筛选待定-筛选中-筛选无效)
            rrv.setClientCount(rrv.getAllClientCount()-rrv.getFilterPendingClientCount()-rrv.getFilterInValidClientCount()-rrv.getFilterInClientCount());
            //合计
            total(total,rrv);
            //计算各种百分比
            everyRate(rrv);

        }
        everyRate(total);
        list.add(0,total);
    }

    private void total(RegionReportsVO total,RegionReportsVO rrv){
        total.setAllClientCount(total.getAllClientCount()+rrv.getAllClientCount());                                 //总客资
        total.setClientCount(total.getClientCount()+rrv.getClientCount());                                          //客资量
        total.setValidClientCount(total.getValidClientCount()+rrv.getValidClientCount());                           //有效量
        total.setInValidClientCount(total.getInValidClientCount()+rrv.getInValidClientCount());                     //无效量
        total.setComeShopClientCount(total.getComeShopClientCount()+rrv.getComeShopClientCount());                  //入店量
        total.setSuccessClientCount(total.getSuccessClientCount()+rrv.getSuccessClientCount());                     //成交量
        total.setPendingClientCount(total.getPendingClientCount()+rrv.getPendingClientCount());                     //待定量
        total.setFilterInClientCount(total.getFilterInValidClientCount()+total.getFilterPendingClientCount());      //筛选中
        total.setFilterInValidClientCount(total.getFilterInValidClientCount()+rrv.getFilterInValidClientCount());   //筛选无效
        total.setFilterPendingClientCount(total.getFilterPendingClientCount()+rrv.getFilterPendingClientCount());   //筛选待定
    }

    /**
     * 计算各种百分比
     * @param rrv
     */
    private void everyRate(RegionReportsVO rrv){
        //有效率
        double validRate = (double) rrv.getValidClientCount() / rrv.getClientCount();
        rrv.setValidRate(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100));
        //无效率
        double invalidRate = (double) rrv.getInValidClientCount() / rrv.getClientCount();
        rrv.setInValidRate(parseDouble(((Double.isNaN(invalidRate) || Double.isInfinite(invalidRate)) ? 0.0 : invalidRate) * 100));
        //待定率
        double waitRate = (double) rrv.getPendingClientCount() / rrv.getClientCount();
        rrv.setWaitRate(parseDouble(((Double.isNaN(waitRate) || Double.isInfinite(waitRate)) ? 0.0 : waitRate) * 100));
        //毛客资入店率
        double clientComeShopRate = (double) rrv.getComeShopClientCount() / rrv.getClientCount();
        rrv.setClientComeShopRate(parseDouble(((Double.isNaN(clientComeShopRate) || Double.isInfinite(clientComeShopRate)) ? 0.0 : clientComeShopRate) * 100));
        //有效客资入店率
        double validComeShopRate = (double) rrv.getComeShopClientCount() / rrv.getValidClientCount();
        rrv.setClientComeShopRate(parseDouble(((Double.isNaN(validComeShopRate) || Double.isInfinite(validComeShopRate)) ? 0.0 : validComeShopRate) * 100));
        //毛客资成交率
        double successRate = (double) rrv.getSuccessClientCount() / rrv.getClientCount();
        rrv.setClientSuccessRate(parseDouble(((Double.isNaN(successRate) || Double.isInfinite(successRate)) ? 0.0 : successRate) * 100));
        //有效客资成交率
        double validSuccessRate = (double) rrv.getSuccessClientCount() / rrv.getValidClientCount();
        rrv.setValidClientSuccessRate(parseDouble(((Double.isNaN(validSuccessRate) || Double.isInfinite(validSuccessRate)) ? 0.0 : validSuccessRate) * 100));
        //入店成交率
        double comeShopSuccessRate = (double) rrv.getSuccessClientCount() / rrv.getComeShopClientCount();
        rrv.setComeShopSuccessRate(parseDouble(((Double.isNaN(comeShopSuccessRate) || Double.isInfinite(comeShopSuccessRate)) ? 0.0 : comeShopSuccessRate) * 100));
    }

    /**
     * 只保留2位小数
     * */
    public double parseDouble(double result){
        return Double.parseDouble(String.format("%.2f",result));
    }
}
