package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgClientInfoCountVO;
import com.qiein.jupiter.web.entity.vo.NumVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电商推广-推广客资统计
 *
 * @Author: shiTao
 */
@Repository
public class DstgClientInfoCountReportsDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * 获取电商推广客资统计 按所有小组
     *
     * @param reportsParamVO
     * @param invalidConfig
     */
    public void getDstgClientInfoCountReports(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        List<DstgClientInfoCountVO> reportsList = new ArrayList<>();
        //统计所有客资
        countAllNum(reportsParamVO, reportsList);
        //毛客资
        Map<String, Integer> kzNumYxNumMap = countKzNumYxNum(reportsParamVO);
        //有效无效
        Map<String, Integer> yxWxNumMap = countYxWxNum(reportsParamVO, invalidConfig);
        //待定
        Map<String, Integer> ddNumAndResetKzNumMap = countDdNumAndResetKzNum(reportsParamVO, invalidConfig);
        //入店
        Map<String, Integer> rdNumMap = countRdNum(reportsParamVO);
        //成交、已收
        Map<String, NumVO> cjNumMap = countCjNum(reportsParamVO);

        //合计及以上汇总
        countTotalAndOther(reportsList, invalidConfig, kzNumYxNumMap,
                yxWxNumMap, ddNumAndResetKzNumMap, rdNumMap, cjNumMap);
        //统计率
        countRate(reportsList);

    }


    /**
     * 统计所有
     *
     * @param reportsParamVO
     * @param reportsList
     */
    private void countAllNum(ReportsParamVO reportsParamVO, final List<DstgClientInfoCountVO> reportsList) {
        final int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("companyId", cid);
        paramsMap.put("start", reportsParamVO.getStart());
        paramsMap.put("end", reportsParamVO.getEnd());
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("SELECT staff_group.GROUPID,staff_group.GROUPNAME,COUNT( * ) COUNT");
        baseSql.append(" FROM ").append(infoTableName).append(" info ").append(" LEFT JOIN ")
                .append(" (SELECT rela.STAFFID,rela.GROUPID,grp.GROUPNAME " +
                        "FROM hm_pub_group_staff rela LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID GROUP BY STAFFID ) " +
                        "staff_group ON info.COLLECTORID = staff_group.STAFFID  ");
        baseSql.append(" WHERE info.COMPANYID = :companyId AND info.ISDEL = 0  ")
                .append(" AND info.CREATETIME  BETWEEN  :start AND :end ")
                .append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 )  GROUP BY staff_group.GROUPID");
        //查询
        namedJdbc.query(baseSql.toString(), paramsMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DstgClientInfoCountVO dstgClientInfoCountVO = new DstgClientInfoCountVO();
                dstgClientInfoCountVO.setCompanyId(cid);
                dstgClientInfoCountVO.setGroupId(resultSet.getString("GROUPID"));
                dstgClientInfoCountVO.setGroupName(resultSet.getString("GROUPNAME"));
                dstgClientInfoCountVO.setNumVO(new NumVO().setNumAll(resultSet.getInt("COUNT")));
                reportsList.add(dstgClientInfoCountVO);
            }
        });

    }

    /**
     * 统计毛客资
     */
    private Map<String, Integer> countKzNumYxNum(ReportsParamVO reportsParamVO) {
        final int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("companyId", cid);
        paramsMap.put("start", reportsParamVO.getStart());
        paramsMap.put("end", reportsParamVO.getEnd());
        paramsMap.put("status1", ClientStatusConst.BE_WAIT_FILTER);
        paramsMap.put("status2", ClientStatusConst.BE_WAIT_WAITING);
        paramsMap.put("status3", ClientStatusConst.BE_FILTER_INVALID);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("SELECT staff_group.GROUPID,staff_group.GROUPNAME,COUNT( * ) COUNT");
        baseSql.append(" FROM ").append(infoTableName).append(" info ").append(" LEFT JOIN ")
                .append(" (SELECT rela.STAFFID,rela.GROUPID,grp.GROUPNAME " +
                        "FROM hm_pub_group_staff rela LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID GROUP BY STAFFID ) " +
                        "staff_group ON info.COLLECTORID = staff_group.STAFFID  ");
        baseSql.append(" WHERE info.COMPANYID = :companyId AND info.ISDEL = 0  ")
                .append(" AND info.CREATETIME  BETWEEN  :start AND :end ")
                .append(" AND info.STATUSID != :status1  AND info.STATUSID != :status2  AND info.STATUSID != :status2 ")
                .append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ")
                .append(" GROUP BY staff_group.GROUPID");
        final Map<String, Integer> groupCountMap = new HashMap<>();
        //查询
        namedJdbc.query(baseSql.toString(), paramsMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                groupCountMap.put(resultSet.getString("GROUPID"), resultSet.getInt("COUNT"));
            }
        });
        return groupCountMap;

    }


    /**
     * 统计有效量、无效量
     */
    private Map<String, Integer> countYxWxNum(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        if (StringUtil.isEmpty(invalidConfig.getDsInvalidLevel())
                && StringUtil.isEmpty(invalidConfig.getDsInvalidStatus())) {
            return null;
        }
        final int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("companyId", cid);
        paramsMap.put("start", reportsParamVO.getStart());
        paramsMap.put("end", reportsParamVO.getEnd());
        paramsMap.put("status1", ClientStatusConst.BE_WAIT_FILTER);
        paramsMap.put("status2", ClientStatusConst.BE_WAIT_WAITING);
        paramsMap.put("status3", ClientStatusConst.BE_FILTER_INVALID);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("SELECT staff_group.GROUPID,staff_group.GROUPNAME,COUNT( * ) COUNT");
        baseSql.append(" FROM ").append(infoTableName).append(" info ").append(" LEFT JOIN ")
                .append(" (SELECT rela.STAFFID,rela.GROUPID,grp.GROUPNAME " +
                        "FROM hm_pub_group_staff rela LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID GROUP BY STAFFID ) " +
                        "staff_group ON info.COLLECTORID = staff_group.STAFFID  ");
        baseSql.append(" WHERE info.COMPANYID = :companyId AND info.ISDEL = 0  ")
                .append(" AND info.CREATETIME  BETWEEN  :start AND :end ")
                .append(" AND info.STATUSID != :status1  AND info.STATUSID != :status2  AND info.STATUSID != :status2 ")
                .append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ")
                .append(" GROUP BY staff_group.GROUPID");
        final Map<String, Integer> groupCountMap = new HashMap<>();
        //查询
        namedJdbc.query(baseSql.toString(), paramsMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                groupCountMap.put(resultSet.getString("GROUPID"), resultSet.getInt("COUNT"));
            }
        });
        return groupCountMap;

    }

    /**
     * 统计待定量 判断待定是否重设置毛客资数量
     */
    private Map<String, Integer> countDdNumAndResetKzNum(ReportsParamVO reportsParamVO, DsInvalidVO invalidConfig) {
        if (StringUtil.isEmpty(invalidConfig.getDsDdStatus())) {
            return null;
        }
        final int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("companyId", cid);
        paramsMap.put("start", reportsParamVO.getStart());
        paramsMap.put("end", reportsParamVO.getEnd());
        paramsMap.put("status", CommonConstant.STR_SEPARATOR + invalidConfig.getDsDdStatus() + CommonConstant.STR_SEPARATOR);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("SELECT staff_group.GROUPID,staff_group.GROUPNAME,COUNT( * ) COUNT");
        baseSql.append(" FROM ").append(infoTableName).append(" info ").append(" LEFT JOIN ")
                .append(" (SELECT rela.STAFFID,rela.GROUPID,grp.GROUPNAME " +
                        "FROM hm_pub_group_staff rela LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID GROUP BY STAFFID ) " +
                        "staff_group ON info.COLLECTORID = staff_group.STAFFID  ");
        baseSql.append(" WHERE info.COMPANYID = :companyId AND info.ISDEL = 0  ")
                .append(" AND info.CREATETIME  BETWEEN  :start AND :end ")
                .append(" AND INSTR( :status, CONCAT(',',info.STATUSID + '',',')) != 0 ")
                .append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ")
                .append(" GROUP BY staff_group.GROUPID");
        final Map<String, Integer> groupCountMap = new HashMap<>();
        //查询
        namedJdbc.query(baseSql.toString(), paramsMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                groupCountMap.put(resultSet.getString("GROUPID"), resultSet.getInt("COUNT"));
            }
        });
        return groupCountMap;
    }

    /**
     * 获取入店量
     */
    private Map<String, Integer> countRdNum(ReportsParamVO reportsParamVO) {
        final int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("companyId", cid);
        paramsMap.put("start", reportsParamVO.getStart());
        paramsMap.put("end", reportsParamVO.getEnd());
//        paramsMap.put("status", ClientStatusConst.IS_COME_SHOP);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("SELECT staff_group.GROUPID,staff_group.GROUPNAME,COUNT( * ) COUNT");
        baseSql.append(" FROM ").append(infoTableName).append(" info ").append(" LEFT JOIN ")
                .append(" (SELECT rela.STAFFID,rela.GROUPID,grp.GROUPNAME " +
                        "FROM hm_pub_group_staff rela LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID GROUP BY STAFFID ) " +
                        "staff_group ON info.COLLECTORID = staff_group.STAFFID  ");
        baseSql.append(" WHERE info.COMPANYID = :companyId AND info.ISDEL = 0  ")
                .append(" AND info.COMESHOPTIME  BETWEEN  :start AND :end ")
                .append(" AND INSTR( :status, CONCAT(',',info.STATUSID + '',',')) != 0 ")
                .append(" AND info.CREATETIME  BETWEEN  :start AND :end ")
//                .append(" AND INSTR( :status, CONCAT(',',info.STATUSID + '',',')) != 0 ")
                .append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ")
                .append(" GROUP BY staff_group.GROUPID");
        final Map<String, Integer> groupCountMap = new HashMap<>();
        //查询
        namedJdbc.query(baseSql.toString(), paramsMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                groupCountMap.put(resultSet.getString("GROUPID"), resultSet.getInt("COUNT"));
            }
        });
        return groupCountMap;
    }

    /**
     * 获取成交量
     */
    private Map<String, NumVO> countCjNum(ReportsParamVO reportsParamVO) {
        final int cid = reportsParamVO.getCompanyId();
        String infoTableName = DBSplitUtil.getInfoTabName(cid);
        //封装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("companyId", cid);
        paramsMap.put("start", reportsParamVO.getStart());
        paramsMap.put("end", reportsParamVO.getEnd());
        paramsMap.put("status", ClientStatusConst.IS_SUCCESS);
        //拼接SQL
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("SELECT staff_group.GROUPID,staff_group.GROUPNAME,COUNT( * ),SUM(det.AMOUNT) YY, SUM(det.STAYAMOUNT) YS ");
        baseSql.append(" FROM ").append(infoTableName).append(" info ").append(" LEFT JOIN ")
                .append(" (SELECT rela.STAFFID,rela.GROUPID,grp.GROUPNAME " +
                        "FROM hm_pub_group_staff rela LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID GROUP BY STAFFID ) " +
                        "staff_group ON info.COLLECTORID = staff_group.STAFFID  ");
        baseSql.append(" WHERE info.COMPANYID = :companyId AND info.ISDEL = 0  ")
                .append(" AND info.SUCCESSTIME  BETWEEN  :start AND :end ")
                .append(" AND INSTR( :status, CONCAT(',',info.STATUSID + '',',')) != 0 ")
                .append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ")
                .append(" GROUP BY staff_group.GROUPID");
        final Map<String, NumVO> groupCountMap = new HashMap<>();
        //查询
        namedJdbc.query(baseSql.toString(), paramsMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                NumVO num = new NumVO();
                num.setCjNum(rs.getInt("COUNT"));
                num.setYyAmount(rs.getDouble("YY"));
                num.setHaveAmount(rs.getDouble("YS"));
                groupCountMap.put(rs.getString("GROUPID"), num);
            }
        });
        return groupCountMap;
    }


    /**
     * 计算合计以及其他的
     */
    private void countTotalAndOther(List<DstgClientInfoCountVO> reportsList, DsInvalidVO invalidConfig,
                                   Map<String, Integer> kzNumMap, Map<String, Integer> yxWxNumMap,
                                   Map<String, Integer> ddNumMap, Map<String, Integer> rdNumMap,
                                   Map<String, NumVO> cjNumMap) {
        NumVO totalNum = new NumVO();
        int numAll = 0;
        int kzNum = 0;
        int wxNum = 0;
        int yxNum = 0;
        int ddNum = 0;
        int rdNum = 0;
        int cjNum = 0;
        double yyAmount = 0.0;
        double haveAmount = 0.0;
        for (DstgClientInfoCountVO dstgClientInfoCountVO : reportsList) {
            String groupId = dstgClientInfoCountVO.getGroupId();

            //设置毛客资 kznum
            dstgClientInfoCountVO.getNumVO().setKzNum(kzNumMap.get(groupId));
            //设置有效无效
            if (yxWxNumMap != null) {

            }
            //设置待定
            if (ddNumMap != null) {
                dstgClientInfoCountVO.getNumVO().setDdNum(ddNumMap.get(groupId));
                //如果企业设置有效包含待定，重新设置有效量

            }
            //设置入店
            dstgClientInfoCountVO.getNumVO().setRdNum(rdNumMap.get(groupId));
            //设置成交量
            dstgClientInfoCountVO.getNumVO().setCjNum(cjNumMap.get(groupId).getCjNum());
            //营业额
            dstgClientInfoCountVO.getNumVO().setYyAmount(cjNumMap.get(groupId).getYyAmount());
            //已收
            dstgClientInfoCountVO.getNumVO().setHaveAmount(cjNumMap.get(groupId).getHaveAmount());
        }

    }

    /**
     * 计算率
     */
    private void countRate(List<DstgClientInfoCountVO> dscjCountReports) {
        for (DstgClientInfoCountVO dscjClientInfo : dscjCountReports) {
            // 有效率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                dscjClientInfo.getNumVO().setYxRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getYxNum()
                        / (double) dscjClientInfo.getNumVO().getKzNum() * 100));
            }
            // 无效率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                dscjClientInfo.getNumVO().setWxRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getWxNum()
                        / (double) dscjClientInfo.getNumVO().getKzNum() * 100));
            }
            // 待定率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                dscjClientInfo.getNumVO().setDdRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getDdNum()
                        / (double) dscjClientInfo.getNumVO().getKzNum() * 100));
            }
            // 毛客资咨询入店率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                dscjClientInfo.getNumVO().setGrossRdRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getRdNum()
                        / (double) dscjClientInfo.getNumVO().getKzNum() * 100));
            }
            // 有效客资咨询入店率
            if (dscjClientInfo.getNumVO().getYxNum() != 0) {
                dscjClientInfo.getNumVO().setRdRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getRdNum()
                        / (double) dscjClientInfo.getNumVO().getYxNum() * 100));
            }
            // 入店成交率
            if (dscjClientInfo.getNumVO().getRdNum() != 0) {
                dscjClientInfo.getNumVO().setCjRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getCjNum()
                        / (double) dscjClientInfo.getNumVO().getRdNum() * 100));
            }
            // 毛客资成交率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                dscjClientInfo.getNumVO().setGrossCjRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getCjNum()
                        / (double) dscjClientInfo.getNumVO().getKzNum() * 100));
            }
            // 有效客资成交率
            if (dscjClientInfo.getNumVO().getYxNum() != 0) {
                dscjClientInfo.getNumVO().setYxCjRate(NumUtil.keep2PointZero(dscjClientInfo.getNumVO().getCjNum()
                        / (double) dscjClientInfo.getNumVO().getYxNum() * 100));
            }
        }
    }

}
