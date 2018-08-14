package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ChinaTerritoryConst;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.vo.RegionReportsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 市域分析报表
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/14 11:18
 */
@Repository
public class CityReportsDao {

    private static final String  PLACEHOLDER = "$Dy$UpUp$";

    /**
     * 获取市域分析报表
     *
     * @param citiesAnalysisParamDTO
     * @return
     */
    public List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO) {

        return null;
    }

    private String getCityReportSQL(CitiesAnalysisParamDTO citiesAnalysisParamDTO) {
        StringBuilder citys = new StringBuilder();
        //从map中找到省份下属的城市
        for (String sid : citiesAnalysisParamDTO.getProvinceNames().split(",")) {
            citys.append(ChinaTerritoryConst.TERRITORY_MAP.get(sid)).append(",");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("")
                .append("FROM")
                .append("(" + getAllClientCount(citiesAnalysisParamDTO.getCompanyId()) + ") zkz ,")
                .append("("+getPendingClientCount(citiesAnalysisParamDTO.getCompanyId())+") ddl ,");
        //TODO 将所有字段的子查询写完后，将其中的占位符替换成指定条件，然后执行

        return null;
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

        return filterInValidClientSQL;
    }

    /**
     * 入店量
     * @param companyId
     * @return
     */
    private StringBuilder getComeShopClientCount(int companyId){
        StringBuilder comeShopClientSQL = new StringBuilder();

        return comeShopClientSQL;
    }

    /**
     * 成交量
     * @param companyId
     * @return
     */
    private StringBuilder getSuccessClientCount(int companyId){
        StringBuilder successClientSQL = new StringBuilder();

        return successClientSQL;
    }

    /**
     * 成交均价
     * @param companyId
     * @return
     */
    private StringBuilder getAvgAmount(int companyId){
        StringBuilder avgAmountSQL = new StringBuilder();

        return avgAmountSQL;
    }

    /**
     * 营业额
     * @param companyId
     * @return
     */
    private StringBuilder getAmount(int companyId){
        StringBuilder amountSQL = new StringBuilder();

        return amountSQL;
    }

    /**
     * 将sql字符串中所有的占位符替换成指定条件
     * @param sb
     * @param searchClientType
     * @return
     */
    private StringBuilder setConditionSQL(StringBuilder sb ,int searchClientType){//查询的客资类型 1所有客资 2 电话客资 3 微信客资 4 qq客资 5 有电话有微信 6 无电话 7.无微信 8 只有电话 9只有微信 10 只有qq
        switch (searchClientType){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
        }
        return sb;
    }
}
