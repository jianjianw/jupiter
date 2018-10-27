package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.web.entity.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电商推广客资统计
 * author :sT
 * cover :xiangliang
 */
@Repository
public class DscjCountReportsDao {
    /**
     * 搜索条件
     */
    private AnalyzeVO searchVO;
    /**
     * 无效指标定义
     */
    private DsInvalidVO dsInvalidVO;
    /**
     * 报表集合
     */
    List<DscjClientInfoCountVO> dscjCountReports;
    /**
     * info表的名字
     */
    String infoTableName;
    /**
     * 详情表的名字
     */
    String detTableName;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 开始业务
     *
     * @return
     * @
     */
    public List<DscjClientInfoCountVO> getDscjTgClientInfoReports(AnalyzeVO vo,DsInvalidVO dsInvalid) {
        // 操作数据库-->
        // 设置搜索条件
        this.searchVO = vo;
        // 返回对象
        Map<String, Object> rst = new HashMap<String, Object>();
        // 报表集合
        dscjCountReports = new ArrayList<>();
        // 获取电商无效指标定义，和待定是否计入有效规则
        dsInvalidVO = dsInvalid;
        infoTableName = DBSplitUtil.getInfoTabName(searchVO.getCompanyId());
        detTableName = DBSplitUtil.getDetailTabName(searchVO.getCompanyId());
        // 设置总客资量
        countDscjCountReports();
        // 设置客资量
        countKzNum();
        // 设置有效无效量
        countYxWxNum();
        // 获取待定及重新设置有效
        countDdNum();
        // 获取入店客资量
        countRdNum();
        // 获取渠道成交客资量
        countCjNum();
        // 计算合计
        countTotal();
        // 计算率和成本
        countRate();
        rst.put("analysis", dscjCountReports);
        return dscjCountReports;
    }

    /**
     * 获取基础select
     *
     * @return
     * @
     */
    private StringBuilder getBaseSelect(String otherColumu, boolean isOrder) {
        StringBuilder selectSql = new StringBuilder();
        selectSql.append(" SELECT grp.GROUPID, grp.GROUPNAME,COUNT( info.KZID ) COUNT  ").append(otherColumu)
                .append(" FROM hm_pub_group grp")
                .append(" LEFT JOIN hm_pub_group_staff rela ON grp.GROUPID = rela.GROUPID LEFT JOIN ")
                .append(infoTableName).append(" info ON info.COLLECTORID = rela.STAFFID ")
                .append(" AND info.ISDEL = 0 ");
        // 如果是订单，时间不一样
        if (isOrder) {
            selectSql.append(" AND info.COMPANYID = ? AND info.SUCCESSTIME >= ? AND info.SUCCESSTIME <= ?  ");
        } else {
            selectSql.append(" AND info.COMPANYID = ? AND info.CREATETIME >= ? AND info.CREATETIME <= ?  ");
        }
        // 判断是否需要加相关条件
        if (StringUtils.isNotEmpty(searchVO.getPhotoTypes())) {
            selectSql.append(" AND info.TYPEID in (" + searchVO.getPhotoTypes() + ") ");
        }
        if (StringUtils.isNotEmpty(searchVO.getSourceIds())) {
            selectSql.append(" AND info.SOURCEID in (" + searchVO.getSourceIds() + ") ");
        }
        selectSql.append(" AND ( info.SRCTYPE = 1 OR info.SRCTYPE = 2 ) ");
        return selectSql;
    }

    /**
     * 设置基础 WHERE
     */
    private StringBuilder getBaseWhere() {
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" WHERE grp.PARENTID != '0' AND grp.COMPANYID = ? AND grp.GROUPTYPE = 'dscj' ");
        // group 语句
        whereSql.append(" GROUP BY grp.GROUPID ");
        whereSql.append(" ORDER BY grp.GROUPNAME ");
        return whereSql;
    }

    /**
     * 获取各个小组的 总客资量
     *
     * @return
     * @
     */
    private void countDscjCountReports() {
        // 基础select
        StringBuilder baseSelect = getBaseSelect("", false);
        // 设置基础where
        StringBuilder baseWhere = getBaseWhere();
        baseSelect.append(baseWhere.toString());
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(baseSelect.toString(),
                new Object[]{searchVO.getCompanyId(), searchVO.getStart(), searchVO.getEnd(), searchVO.getCompanyId()});
        for (Map<String, Object> map : dstgGoldDataReports) {
            DscjClientInfoCountVO dscjClientInfoCountVO = new DscjClientInfoCountVO();
            dscjClientInfoCountVO.setGroupId(String.valueOf(map.get("GROUPID")));
            dscjClientInfoCountVO.setGroupName(String.valueOf(map.get("GROUPNAME")));
            dscjClientInfoCountVO.setNumVO(new NumVO().setNumAll(Integer.parseInt(String.valueOf(map.get("COUNT")))));
            dscjCountReports.add(dscjClientInfoCountVO);

        }
    }

    /**
     * 获取客资量
     *
     * @
     */
    private void countKzNum() {
        // 基础select
        StringBuilder baseSelect = getBaseSelect("", false);
        baseSelect.append(" AND info.STATUSID != ?  AND info.STATUSID != ?  AND info.STATUSID != ? ");
        // 其余的where
        // 设置基础where
        StringBuilder baseWhere = getBaseWhere();
        baseSelect.append(baseWhere.toString());
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(baseSelect.toString(),
                new Object[]{searchVO.getCompanyId(), searchVO.getStart(), searchVO.getEnd(), ClientStatusConst.BE_FILTER_INVALID, ClientStatusConst.BE_WAIT_FILTER, ClientStatusConst.BE_WAIT_WAITING, searchVO.getCompanyId()});
        // 组的值统计Map
        Map<String, Integer> numMap = new HashMap<>();
        for (Map<String, Object> map : dstgGoldDataReports) {
            numMap.put(String.valueOf(map.get("GROUPID")), Integer.parseInt(String.valueOf(map.get("COUNT"))));
        }
        // 循环遍历 设置值
        for (DscjClientInfoCountVO dscjClientInfoCountVO : dscjCountReports) {
            Integer count = numMap.get(dscjClientInfoCountVO.getGroupId());
            dscjClientInfoCountVO.getNumVO().setKzNum(count);
            dscjClientInfoCountVO.getNumVO().setYxNum(count);
        }
    }

    /**
     * 获取 有效 和 无效量 ,有效 = 客资量-无效
     */
    private void countYxWxNum() {
        if (StringUtils.isEmpty(dsInvalidVO.getDsInvalidLevel())
                && StringUtils.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            return;
        }
        StringBuilder baseSelect = getBaseSelect("", false);
        boolean flag = true;

        // 筛选无效状态
        if (StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            flag = false;
            baseSelect.append(" AND ( ");
            baseSelect.append(
                    " ( info.CLASSID = 2 AND INSTR( CONCAT(',', '"+CommonConstant.STR_SEPARATOR + dsInvalidVO.getDsInvalidLevel() + CommonConstant.STR_SEPARATOR+"' ,','), CONCAT(',',det.YXLEVEL+'',',') ) != 0 ) ");
            baseSelect.append(" ) ");
        }
        // 筛选无效状态
//        if (StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidStatus())) {
//            String[] statusArr = dsInvalidVO.getDsInvalidStatus().split(CommonConstant.STR_SEPARATOR);
//            for (int i = 0; i < statusArr.length; i++) {
//                if (StringUtils.isEmpty(statusArr[i])) {
//                    continue;
//                }
//                if (i == 0 && flag) {
//                    baseSelect.append(" info.STATUSID = ");
//                    baseSelect.append(Integer.valueOf(statusArr[i]));
//                } else {
//                    baseSelect.append(" OR info.STATUSID = ");
//                    baseSelect.append(Integer.valueOf(statusArr[i]));
//                }
//            }
//            flag = false;
//        }

        // 加入客资详情表
        baseSelect.append(" LEFT JOIN ");
        baseSelect.append(detTableName).append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        // 筛选无效的意向等级
        if (StringUtils.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            baseSelect.append(
                    "  AND ( info.CLASSID = 2 AND INSTR( CONCAT(',', '"+CommonConstant.STR_SEPARATOR + dsInvalidVO.getDsInvalidLevel() + CommonConstant.STR_SEPARATOR+"' ,','), CONCAT(',',det.YXLEVEL+'',',') ) != 0 ) ");
        }

        // 设置基础where
        StringBuilder baseWhere = getBaseWhere();
        baseSelect.append(baseWhere.toString());

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(baseSelect.toString(),
                new Object[]{searchVO.getCompanyId(), searchVO.getStart(), searchVO.getEnd(), searchVO.getCompanyId()});
        // 组的值统计Map
        Map<String, Integer> numMap = new HashMap<>();
        for (Map<String, Object> map : dstgGoldDataReports) {
            numMap.put(String.valueOf(map.get("GROUPID")), Integer.parseInt(String.valueOf(map.get("COUNT"))));
        }
        // 循环遍历 设置值
        for (DscjClientInfoCountVO dscjClientInfoCountVO : dscjCountReports) {
            Integer count = numMap.get(dscjClientInfoCountVO.getGroupId());
            dscjClientInfoCountVO.getNumVO().setWxNum(count);
            int kzNum = dscjClientInfoCountVO.getNumVO().getKzNum();
            // 有效 = 客资量-无效
            dscjClientInfoCountVO.getNumVO().setYxNum(kzNum - count);
        }

    }

    /**
     * 获取待定量，有效 = 客资量- 无效量 - 待定量
     */
    private void countDdNum() {
        if (StringUtils.isEmpty(dsInvalidVO.getDsDdStatus())) {
            return;
        }
        StringBuilder baseSelect = getBaseSelect("", false);
        baseSelect.append(" AND INSTR( '"+CommonConstant.STR_SEPARATOR + dsInvalidVO.getDsDdStatus() + CommonConstant.STR_SEPARATOR+"', CONCAT(',',info.STATUSID + '',',')) != 0");
        // 设置基础where
        StringBuilder baseWhere = getBaseWhere();
        baseSelect.append(baseWhere.toString());

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(baseSelect.toString(),
                new Object[]{searchVO.getCompanyId(), searchVO.getStart(), searchVO.getEnd(), searchVO.getCompanyId()});
        // 组的值统计Map
        Map<String, Integer> numMap = new HashMap<>();
        for (Map<String, Object> map : dstgGoldDataReports) {
            numMap.put(String.valueOf(map.get("GROUPID")), Integer.parseInt(String.valueOf(map.get("COUNT"))));
        }
        // 循环遍历 设置值
        for (DscjClientInfoCountVO dscjClientInfoCountVO : dscjCountReports) {
            Integer count = numMap.get(dscjClientInfoCountVO.getGroupId());
            dscjClientInfoCountVO.getNumVO().setDdNum(count);
            int yxNum = dscjClientInfoCountVO.getNumVO().getYxNum();
            // 判断下无效定义里面的值
            if (!dsInvalidVO.getDdIsValid()) {
                // 根据有效定义，重新设置下有效
                dscjClientInfoCountVO.getNumVO().setYxNum(yxNum - count);
            }
        }
    }

    /**
     * 设置入店量
     */
    private void countRdNum() {
        StringBuilder baseSelect = getBaseSelect("", false);
//		baseSelect.append(" AND INSTR( ?, CONCAT(',',info.STATUSID + '',',')) != 0");
//		// 到店
//		fieldList.add(ClientStatusConst.IS_COME_SHOP);
        // 设置基础where
        StringBuilder baseWhere = getBaseWhere();
        baseSelect.append(baseWhere.toString());

        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(baseSelect.toString(),
                new Object[]{searchVO.getCompanyId(), searchVO.getStart(), searchVO.getEnd(), searchVO.getCompanyId()});
        // 组的值统计Map
        Map<String, Integer> numMap = new HashMap<>();
        for (Map<String, Object> map : dstgGoldDataReports) {
            numMap.put(String.valueOf(map.get("GROUPID")), Integer.parseInt(String.valueOf(map.get("COUNT"))));
        }
        // 循环遍历 设置值
        for (DscjClientInfoCountVO dscjClientInfoCountVO : dscjCountReports) {
            Integer count = numMap.get(dscjClientInfoCountVO.getGroupId());
            // 入店量
            dscjClientInfoCountVO.getNumVO().setRdNum(count);
        }
    }

    /**
     * 获取成交量
     */
    private void countCjNum() {
        StringBuilder baseSelect = getBaseSelect(" ,ifnull(SUM(det.AMOUNT),0) YY, ifnull(SUM(det.STAYAMOUNT),0) YS ", true);
        baseSelect.append(" AND INSTR( '"+ClientStatusConst.IS_SUCCESS+"', CONCAT(',',info.STATUSID + '',',')) != 0 ");
        baseSelect.append(" LEFT JOIN ");
        baseSelect.append(detTableName).append(" det ON det.KZID = info.KZID AND det.COMPANYID = info.COMPANYID ");
        // 设置基础where
        StringBuilder baseWhere = getBaseWhere();
        baseSelect.append(baseWhere.toString());
        List<Map<String, Object>> dstgGoldDataReports = jdbcTemplate.queryForList(baseSelect.toString(),
                new Object[]{searchVO.getCompanyId(), searchVO.getStart(), searchVO.getEnd(), searchVO.getCompanyId()});
        // 组的值统计Map
        Map<String, NumVO> numMap = new HashMap<>();
        for (Map<String, Object> map : dstgGoldDataReports) {
            NumVO num = new NumVO();
            num.setCjNum(Integer.parseInt(String.valueOf(map.get("COUNT"))));
            num.setYyAmount(Integer.parseInt(String.valueOf(map.get("YY"))));
            num.setHaveAmount(Integer.parseInt(String.valueOf(map.get("YY"))));
            numMap.put(String.valueOf(map.get("GROUPID")), num);
        }
        // 循环遍历 设置值
        for (DscjClientInfoCountVO dscjClientInfoCountVO : dscjCountReports) {
            NumVO num = numMap.get(dscjClientInfoCountVO.getGroupId());
            // 成交量
            dscjClientInfoCountVO.getNumVO().setCjNum(num.getCjNum());
            // 营业额
            dscjClientInfoCountVO.getNumVO().setYyAmount(num.getYyAmount());
            // 已收金额
            dscjClientInfoCountVO.getNumVO().setHaveAmount(num.getHaveAmount());
        }
    }

    /**
     * 统计所有合计
     */
    private void countTotal() {
        DscjClientInfoCountVO dscjClientInfoCountTotal = new DscjClientInfoCountVO();
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
        for (DscjClientInfoCountVO dscjClientInfoCount : dscjCountReports) {
            // 总客资
            numAll += dscjClientInfoCount.getNumVO().getNumAll();
            // 客资
            kzNum += dscjClientInfoCount.getNumVO().getKzNum();
            // 有效
            yxNum += dscjClientInfoCount.getNumVO().getYxNum();
            // 无效
            wxNum += dscjClientInfoCount.getNumVO().getWxNum();
            // 待定
            ddNum += dscjClientInfoCount.getNumVO().getDdNum();
            // 入店
            rdNum += dscjClientInfoCount.getNumVO().getRdNum();
            // 成交
            cjNum += dscjClientInfoCount.getNumVO().getCjNum();
            // 营业额
            yyAmount += Double.valueOf(dscjClientInfoCount.getNumVO().getYyAmount());
            // 已收
            haveAmount += Double.valueOf(dscjClientInfoCount.getNumVO().getHaveAmount());
        }
        totalNum.setNumAll(numAll);
        totalNum.setKzNum(kzNum);
        totalNum.setYxNum(yxNum);
        totalNum.setWxNum(wxNum);
        totalNum.setDdNum(ddNum);
        totalNum.setRdNum(rdNum);
        totalNum.setCjNum(cjNum);
        totalNum.setYyAmount(yyAmount);
        totalNum.setHaveAmount(haveAmount);
        dscjClientInfoCountTotal.setNumVO(totalNum);
        dscjClientInfoCountTotal.setGroupId("-1");
        dscjClientInfoCountTotal.setGroupName("合计");
        dscjCountReports.add(0, dscjClientInfoCountTotal);
    }

    /**
     * 计算各种率
     */
    private void countRate() {
        for (DscjClientInfoCountVO dscjClientInfo : dscjCountReports) {
            // 有效率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getYxNum()/ dscjClientInfo.getNumVO().getKzNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
//                dscjClientInfo.getNumVO().setYxRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getYxNum()
//                        / Double.valueOf(dscjClientInfo.getNumVO().getKzNum()) * 100));
            }
            // 无效率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getWxNum() / dscjClientInfo.getNumVO().getKzNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
              //  dscjClientInfo.getNumVO().setWxRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getWxNum() / Double.valueOf(dscjClientInfo.getNumVO().getKzNum()) * 100));
            }
            // 待定率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getDdNum() / dscjClientInfo.getNumVO().getKzNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
               // dscjClientInfo.getNumVO().setDdRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getDdNum() / Double.valueOf(dscjClientInfo.getNumVO().getKzNum()) * 100));
            }
            // 毛客资咨询入店率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getRdNum()/ dscjClientInfo.getNumVO().getKzNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
              //  dscjClientInfo.getNumVO().setGrossRdRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getRdNum() / Double.valueOf(dscjClientInfo.getNumVO().getKzNum()) * 100));
            }
            // 有效客资咨询入店率
            if (dscjClientInfo.getNumVO().getYxNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getRdNum()/ dscjClientInfo.getNumVO().getYxNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
                //dscjClientInfo.getNumVO().setRdRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getRdNum() / Double.valueOf(dscjClientInfo.getNumVO().getYxNum()) * 100));
            }
            // 入店成交率
            if (dscjClientInfo.getNumVO().getRdNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getCjNum()/ dscjClientInfo.getNumVO().getRdNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
               // dscjClientInfo.getNumVO().setCjRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getCjNum() / Double.valueOf(dscjClientInfo.getNumVO().getRdNum()) * 100));
            }
            // 毛客资成交率
            if (dscjClientInfo.getNumVO().getKzNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getCjNum()/ dscjClientInfo.getNumVO().getKzNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
                //dscjClientInfo.getNumVO().setGrossCjRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getCjNum() / Double.valueOf(dscjClientInfo.getNumVO().getKzNum()) * 100));
            }
            // 有效客资成交率
            if (dscjClientInfo.getNumVO().getYxNum() != 0) {
                double validRate = (double)dscjClientInfo.getNumVO().getCjNum()/ dscjClientInfo.getNumVO().getYxNum();
                dscjClientInfo.getNumVO().setYxRate(String.valueOf(parseDouble(((Double.isNaN(validRate) || Double.isInfinite(validRate)) ? 0.0 : validRate) * 100)));
                //dscjClientInfo.getNumVO().setYxCjRate(StringUtils.decimalFormat(dscjClientInfo.getNumVO().getCjNum() / Double.valueOf(dscjClientInfo.getNumVO().getYxNum()) * 100));
            }
        }
    }

    /**
     * 获取无效指标定义规则
     *
     * @param companyId
     * @return
     */
//    private DsInvalidVO getInvalidConfig(int companyId) {
//        StringBuilder sb = new StringBuilder();
//        DsInvalidVO dsInvalidVO = new DsInvalidVO();
//        if (IntegerUtils.isInValid(companyId)) {
//            return null;
//        }
//        sb.append(
//                " SELECT comp.DSINVALIDSTATUS, comp.DSINVALIDLEVEL, comp.DDISVALID, comp.DSDDSTATUS  FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
//
//        fieldList.clear();
//        fieldList.add(companyId);
//
//        RowSet rs = null;
//        try {
//            rs = dbSession.executeQuery(sb.toString(), fieldList);
//            if (RowSetUtils.isNotEmpty(rs)) {
//                RowSetUtils.first(rs);
//                dsInvalidVO.setDsInvalidStatus(RowSetUtils.getString(rs, "DSINVALIDSTATUS"));
//                dsInvalidVO.setDsInvalidLevel(RowSetUtils.getString(rs, "DSINVALIDLEVEL"));
//                dsInvalidVO.setDdIsValid(RowSetUtils.getBoolean(rs, "DDISVALID"));
//                dsInvalidVO.setDsDdStatus(RowSetUtils.getString(rs, "DSDDSTATUS"));
//            }
//        } catch (Exception e) {
//
//        }
//        return dsInvalidVO;
//    }
    /**
     * 只保留2位小数
     */
    public double parseDouble(double result) {
        return Double.parseDouble(String.format("%.2f", result));
    }
}
